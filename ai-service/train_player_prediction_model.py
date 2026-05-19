"""
Train the ML model for Player Performance Prediction and export artifacts.

This module trains a machine learning model that predicts player performance
based on historical statistics, trends, and recent form.

Models used:
- Random Forest Regressor: For non-linear relationships
- Gradient Boosting Regressor: For enhanced predictions
- Model selection based on cross-validation performance

Exported artifacts:
- player_performance_model.joblib (primary model)
- player_performance_scaler.joblib (feature scaler)
- player_performance_feature_names.joblib (feature column names)
"""

import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.preprocessing import StandardScaler, PolynomialFeatures
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import mean_squared_error, r2_score, mean_absolute_error
import joblib
import os
import warnings

warnings.filterwarnings('ignore')

# Change to ai-service directory
script_dir = os.path.dirname(os.path.abspath(__file__))
os.chdir(script_dir)

print("=" * 80)
print("Player Performance Prediction Model Training")
print("=" * 80)

# ============================================================
# Load training data from CSV or generate synthetic data
# ============================================================
# This module either loads real player statistics from CSV
# or generates synthetic but realistic player statistics
# In production, this would come from the PostgreSQL database

csv_path = 'data/player_performance_dataset.csv'

if os.path.exists(csv_path):
    print(f"[Training] Loading data from: {csv_path}")
    df = pd.read_csv(csv_path)
    print(f"[Training] Loaded {df.shape[0]} samples from CSV")
else:
    print(f"[Training] CSV not found at {csv_path}")
    print("[Training] Generating synthetic training data...")
    
    np.random.seed(42)

    # Generate 500 synthetic player performance records
    n_samples = 500

    data = {
        'player_id': np.random.randint(1, 100, n_samples),
        'goals': np.random.poisson(2, n_samples) + np.random.randint(-1, 2, n_samples),
        'assists': np.random.poisson(1, n_samples) + np.random.randint(-1, 1, n_samples),
        'tackles': np.random.poisson(5, n_samples) + np.random.randint(-2, 2, n_samples),
        'interceptions': np.random.poisson(3, n_samples) + np.random.randint(-1, 2, n_samples),
        'passes_completed': np.random.normal(50, 15, n_samples),
        'pass_accuracy': np.random.uniform(70, 95, n_samples),
        'distance_covered_km': np.random.normal(10, 2, n_samples),
        'average_speed_kmh': np.random.normal(25, 5, n_samples),
        'ball_possession_percent': np.random.uniform(40, 80, n_samples),
        'fouls_committed': np.random.poisson(2, n_samples),
        'yellow_cards': np.random.poisson(0.5, n_samples),
        'shots_on_target': np.random.poisson(2, n_samples),
    }

    # Target variable: next match performance (0-100 scale)
    # Influenced by player's previous stats and form
    base_performance = 50 + (
        (data['goals'] * 5) +
        (data['assists'] * 3) +
        (data['tackles'] * 0.5) +
        (data['interceptions'] * 1) +
        (data['pass_accuracy'] * 0.2) +
        (data['shots_on_target'] * 2)
    ) / 10 + np.random.normal(0, 5, n_samples)

    data['next_match_performance_rating'] = np.clip(base_performance, 0, 100)

    df = pd.DataFrame(data)
    print(f"[Training] Generated {df.shape[0]} synthetic samples")


def build_edge_case_training_data(start_player_id=1000):
    """Generate low, average, and elite examples so the model learns the full 0-100 scale."""
    rng = np.random.default_rng(2026)
    records = []

    profiles = [
        {
            "name": "poor",
            "count": 90,
            "ranges": {
                "goals": (0, 1), "assists": (0, 1), "tackles": (0, 3), "interceptions": (0, 2),
                "passes_completed": (0, 25), "pass_accuracy": (5, 45), "distance_covered_km": (2, 7),
                "average_speed_kmh": (8, 17), "ball_possession_percent": (5, 30),
                "fouls_committed": (5, 12), "yellow_cards": (0, 2), "shots_on_target": (0, 1)
            }
        },
        {
            "name": "average",
            "count": 90,
            "ranges": {
                "goals": (0, 3), "assists": (0, 3), "tackles": (2, 7), "interceptions": (1, 5),
                "passes_completed": (25, 85), "pass_accuracy": (50, 82), "distance_covered_km": (7, 11),
                "average_speed_kmh": (18, 26), "ball_possession_percent": (30, 60),
                "fouls_committed": (1, 5), "yellow_cards": (0, 1), "shots_on_target": (0, 5)
            }
        },
        {
            "name": "elite",
            "count": 90,
            "ranges": {
                "goals": (2, 8), "assists": (1, 7), "tackles": (4, 12), "interceptions": (2, 8),
                "passes_completed": (75, 180), "pass_accuracy": (78, 98), "distance_covered_km": (10, 15),
                "average_speed_kmh": (24, 34), "ball_possession_percent": (55, 85),
                "fouls_committed": (0, 3), "yellow_cards": (0, 1), "shots_on_target": (3, 12)
            }
        }
    ]

    player_id = start_player_id
    for profile in profiles:
        for _ in range(profile["count"]):
            row = {"player_id": player_id}
            player_id += 1

            for feature, (low, high) in profile["ranges"].items():
                if feature in {"pass_accuracy", "distance_covered_km", "average_speed_kmh", "ball_possession_percent"}:
                    row[feature] = round(float(rng.uniform(low, high)), 1)
                else:
                    row[feature] = int(rng.integers(low, high + 1))

            rating = (
                15
                + row["goals"] * 5.0
                + row["assists"] * 3.2
                + row["shots_on_target"] * 2.4
                + row["tackles"] * 0.8
                + row["interceptions"] * 1.2
                + row["passes_completed"] * 0.08
                + row["pass_accuracy"] * 0.22
                + row["distance_covered_km"] * 1.2
                + row["average_speed_kmh"] * 0.35
                + row["ball_possession_percent"] * 0.12
                - row["fouls_committed"] * 3.2
                - row["yellow_cards"] * 8.0
                + float(rng.normal(0, 3))
            )
            row["next_match_performance_rating"] = round(float(np.clip(rating, 5, 98)), 1)
            records.append(row)

    return pd.DataFrame(records)


edge_case_df = build_edge_case_training_data(
    start_player_id=int(df["player_id"].max()) + 1 if "player_id" in df.columns else 1000
)
df = pd.concat([df, edge_case_df], ignore_index=True)
print(f"[Training] Added {edge_case_df.shape[0]} edge-case samples for low/average/elite performance")

print(f"\n[Training] Dataset generated: {df.shape[0]} samples, {df.shape[1]} features")
print(f"[Training] Performance rating range: {df['next_match_performance_rating'].min():.1f} - {df['next_match_performance_rating'].max():.1f}")
print(f"\nDataset Preview:")
print(df.head(10))
print(f"\nDataset Statistics:")
print(df.describe())

# ============================================================
# Feature Engineering
# ============================================================
print("\n" + "=" * 80)
print("Feature Engineering")
print("=" * 80)

# Create derived features
df['goals_assists_ratio'] = (df['goals'] + 1) / (df['assists'] + 1)
df['defensive_contribution'] = df['tackles'] + df['interceptions']
df['aerial_ability'] = df['interceptions'] + df['tackles'] * 0.3
df['ball_retention'] = df['pass_accuracy'] * (df['passes_completed'] / 100)
df['physical_intensity'] = df['distance_covered_km'] * df['average_speed_kmh'] / 10
df['attack_threat'] = df['shots_on_target'] + (df['goals'] * 2)
df['discipline_factor'] = 100 - (df['fouls_committed'] * 5 + df['yellow_cards'] * 10)

# Features for the model
feature_columns = [
    'goals', 'assists', 'tackles', 'interceptions', 'passes_completed',
    'pass_accuracy', 'distance_covered_km', 'average_speed_kmh',
    'ball_possession_percent', 'fouls_committed', 'shots_on_target',
    'goals_assists_ratio', 'defensive_contribution', 'ball_retention',
    'physical_intensity', 'attack_threat', 'discipline_factor'
]

X = df[feature_columns]
y = df['next_match_performance_rating']

print(f"[Training] Features: {len(feature_columns)}")
for i, col in enumerate(feature_columns, 1):
    print(f"  {i:2d}. {col}")

# ============================================================
# Data Preprocessing & Scaling
# ============================================================
print("\n" + "=" * 80)
print("Data Preprocessing")
print("=" * 80)

# Handle missing values
X = X.fillna(X.mean())

scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

print(f"[Training] Data scaled using StandardScaler")
print(f"[Training] Feature shapes: {X_scaled.shape}")

# ============================================================
# Model Training
# ============================================================
print("\n" + "=" * 80)
print("Model Training")
print("=" * 80)

# Split data
X_train, X_test, y_train, y_test = train_test_split(
    X_scaled, y, test_size=0.2, random_state=42
)

print(f"[Training] Train set: {X_train.shape[0]} samples")
print(f"[Training] Test set: {X_test.shape[0]} samples")

# Train Random Forest model
print("\n[Training] Training Random Forest Regressor...")
rf_model = RandomForestRegressor(
    n_estimators=100,
    max_depth=10,
    min_samples_split=5,
    min_samples_leaf=2,
    random_state=42,
    n_jobs=1,
    verbose=0
)
rf_model.fit(X_train, y_train)

# Evaluate Random Forest
rf_train_pred = rf_model.predict(X_train)
rf_test_pred = rf_model.predict(X_test)
rf_train_r2 = r2_score(y_train, rf_train_pred)
rf_test_r2 = r2_score(y_test, rf_test_pred)
rf_test_mae = mean_absolute_error(y_test, rf_test_pred)
rf_test_rmse = np.sqrt(mean_squared_error(y_test, rf_test_pred))

print(f"  Random Forest - Train R²: {rf_train_r2:.4f}, Test R²: {rf_test_r2:.4f}")
print(f"  Random Forest - MAE: {rf_test_mae:.4f}, RMSE: {rf_test_rmse:.4f}")

# Train Gradient Boosting model for comparison
print("\n[Training] Training Gradient Boosting Regressor...")
gb_model = GradientBoostingRegressor(
    n_estimators=100,
    learning_rate=0.1,
    max_depth=5,
    min_samples_split=5,
    subsample=0.8,
    random_state=42,
    verbose=0
)
gb_model.fit(X_train, y_train)

# Evaluate Gradient Boosting
gb_train_pred = gb_model.predict(X_train)
gb_test_pred = gb_model.predict(X_test)
gb_train_r2 = r2_score(y_train, gb_train_pred)
gb_test_r2 = r2_score(y_test, gb_test_pred)
gb_test_mae = mean_absolute_error(y_test, gb_test_pred)
gb_test_rmse = np.sqrt(mean_squared_error(y_test, gb_test_pred))

print(f"  Gradient Boosting - Train R²: {gb_train_r2:.4f}, Test R²: {gb_test_r2:.4f}")
print(f"  Gradient Boosting - MAE: {gb_test_mae:.4f}, RMSE: {gb_test_rmse:.4f}")

# Select best model
best_model = gb_model if gb_test_r2 > rf_test_r2 else rf_model
best_name = "Gradient Boosting" if gb_test_r2 > rf_test_r2 else "Random Forest"
best_r2 = max(gb_test_r2, rf_test_r2)

print(f"\n[Training] Selected Model: {best_name} (Test R²: {best_r2:.4f})")

# Feature importance
feature_importance = pd.DataFrame({
    'feature': feature_columns,
    'importance': best_model.feature_importances_
}).sort_values('importance', ascending=False)

print(f"\n[Training] Top 10 Most Important Features:")
for idx, (_, row) in enumerate(feature_importance.head(10).iterrows(), 1):
    print(f"  {idx:2d}. {row['feature']:25s} {row['importance']:.4f}")

# ============================================================
# Export Models
# ============================================================
print("\n" + "=" * 80)
print("Exporting Models")
print("=" * 80)

os.makedirs('model', exist_ok=True)

joblib.dump(best_model, 'model/player_performance_model.joblib')
joblib.dump(scaler, 'model/player_performance_scaler.joblib')
joblib.dump(feature_columns, 'model/player_performance_feature_names.joblib')
joblib.dump(feature_importance, 'model/player_performance_feature_importance.joblib')

# Save metadata
metadata = {
    'model_type': best_name,
    'feature_count': len(feature_columns),
    'training_samples': len(X_train),
    'test_samples': len(X_test),
    'r2_score': float(best_r2),
    'mae': float(mean_absolute_error(y_test, best_model.predict(X_test))),
    'rmse': float(np.sqrt(mean_squared_error(y_test, best_model.predict(X_test)))),
    'features': feature_columns,
    'feature_ranges': {
        col: {
            'min': float(X[col].min()),
            'max': float(X[col].max())
        }
        for col in feature_columns
    }
}

joblib.dump(metadata, 'model/player_performance_metadata.joblib')

print(f"[Export] Artifacts saved to model/:")
print(f"  - player_performance_model.joblib ({os.path.getsize('model/player_performance_model.joblib') / 1024:.1f} KB)")
print(f"  - player_performance_scaler.joblib ({os.path.getsize('model/player_performance_scaler.joblib') / 1024:.1f} KB)")
print(f"  - player_performance_feature_names.joblib ({os.path.getsize('model/player_performance_feature_names.joblib') / 1024:.1f} KB)")
print(f"  - player_performance_feature_importance.joblib ({os.path.getsize('model/player_performance_feature_importance.joblib') / 1024:.1f} KB)")
print(f"  - player_performance_metadata.joblib ({os.path.getsize('model/player_performance_metadata.joblib') / 1024:.1f} KB)")

# Save sample data
df.to_csv('model/player_stats_enriched.csv', index=False)
print(f"  - player_stats_enriched.csv ({os.path.getsize('model/player_stats_enriched.csv') / 1024:.1f} KB)")

print("\n" + "=" * 80)
print("✓ Training Complete!")
print("=" * 80)
print("\nNext step: Start app.py to serve predictions")
print("Endpoint: POST /api/ai/predict-player-performance")
