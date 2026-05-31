# 🧪 Complete Testing Guide - AI Features

> **Target:** Test all 3 AI prediction features  
> **Duration:** 20-30 minutes  
> **Complexity:** Beginner-friendly  

---

## 📋 Quick Reference: What We're Testing

| Feature | Type | ML Algorithm | Input Data | Output |
|---------|------|--------------|-----------|--------|
| **Future Player Performance** | Prediction | Gradient Boosting (17 features) | Player stats CSV | 0-100 rating |
| **Advanced Métier 1** | Basic Prediction | Linear Regression + Trend | Historical matches | Rating + trend |
| **Advanced Métier 2** | Advanced Prediction | ML Ensemble | 17 engineered features | Rating + analysis |

---

## ✅ STEP 1: Pre-Test Setup (5 minutes)

### 1.1 Verify Data Exists

```bash
# Check if player performance dataset exists
ls -la d:\PI2027\PI_StreetLeague\ai-service\data\

# Expected: player_performance_dataset.csv should be listed
# Size: ~3-5 KB (50 player records)
```

### 1.2 Verify Services Are Running

**Terminal 1 - Python AI Service:**
```bash
cd d:\PI2027\PI_StreetLeague\ai-service
python app.py
```

Expected output:
```
[AI Service] ✓ Exercise recommendation model loaded
[AI Service] ✓ Player performance model loaded  
[AI Service] Running on http://localhost:5000
```

**Terminal 2 - Java Backend:**
```bash
cd d:\PI2027\PI_StreetLeague\backend
./mvnw spring-boot:run
```

Expected output:
```
Started StreetLeagueBackendApplication in 25.xxx seconds
```

### 1.3 Verify Health Endpoints

```bash
# Check Python service health
curl http://localhost:5000/api/ai/health

# Expected response:
# {"status": "ok", "player_prediction_model_loaded": true, ...}

# Check Java service health
curl http://localhost:8080/api/players/ai/health

# Expected response:
# {"status": "ok", "player_prediction_model_loaded": true}
```

---

## 🔧 STEP 2: Train ML Models (ONE TIME - 2 minutes)

Before testing predictions, train the models:

```bash
cd d:\PI2027\PI_StreetLeague\ai-service

# This loads player_performance_dataset.csv and trains models
python train_player_prediction_model.py
```

**Expected Output:**
```
================================================================================
Player Performance Prediction Model Training
================================================================================

[Training] Loading data from: data/player_performance_dataset.csv
[Training] Loaded 50 samples from CSV

================================================================================
Feature Engineering
================================================================================

[Training] Features: 17
  1. goals
  2. assists
  ...
  17. discipline_factor

================================================================================
Model Training
================================================================================

[Training] Train set: 40 samples
[Training] Test set: 10 samples
[Training] Random Forest R² Score: 0.85
[Training] Gradient Boosting R² Score: 0.88

[Training] Selected model: Gradient Boosting Regressor
[Training] Cross-validation R² (5-fold): 0.84 ± 0.06

[Export] ✓ Model exported to: model/player_performance_model.joblib
[Export] ✓ Scaler exported to: model/player_performance_scaler.joblib
[Export] ✓ Features exported to: model/player_performance_feature_names.joblib
[Export] ✓ Metadata exported to: model/player_performance_metadata.joblib

[Training] ✅ Training complete! All artifacts ready.
```

**Verify Model Files Created:**
```bash
ls -la d:\PI2027\PI_StreetLeague\ai-service\model\

# Expected files:
# - player_performance_model.joblib (~500 KB)
# - player_performance_scaler.joblib (~2 KB)
# - player_performance_feature_names.joblib (~1 KB)
# - player_performance_metadata.joblib (~1 KB)
```

---

## 🎯 FEATURE 1: Future Player Performance Prediction

### Overview
- **What:** Predicts a player's next match performance (0-100 scale)
- **How:** Uses Gradient Boosting ML model on 17 engineered features
- **Data Source:** Historical player statistics (CSV dataset)
- **Algorithm:** Gradient Boosting Regressor (R² ≈ 0.88)

### Features Used (17 Total)

**Base Statistics (12):**
```
goals, assists, tackles, interceptions, passes_completed, 
pass_accuracy, distance_covered_km, average_speed_kmh, 
ball_possession_percent, fouls_committed, yellow_cards, shots_on_target
```

**Engineered Features (5):**
```
goals_assists_ratio       = (goals + 1) / (assists + 1)
defensive_contribution    = tackles + interceptions
ball_retention           = pass_accuracy * (passes_completed / 100)
physical_intensity       = distance_covered_km * average_speed_kmh / 10
attack_threat            = shots_on_target + (goals * 2)
discipline_factor        = 100 - (fouls_committed * 5 + yellow_cards * 10)
```

---

### Test 1.1: Custom AI Prediction (Best for Testing)

**Use Case:** Test with custom player statistics

```bash
# Terminal 3 - Test with custom player stats
curl -X POST http://localhost:8080/api/players/ai-predict-custom \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": 101,
    "goals": 3,
    "assists": 2,
    "tackles": 5,
    "interceptions": 3,
    "passesCompleted": 52,
    "passAccuracy": 87.5,
    "distanceCoveredKm": 10.5,
    "averageSpeedKmh": 25.8,
    "ballPossessionPercent": 51.0,
    "foulsCommitted": 1,
    "shotsOnTarget": 4
  }'
```

**Expected Response:**
```json
{
  "playerId": 101,
  "predictedPerformanceRating": 76.5,
  "performanceCategory": "EXCELLENT",
  "interpretation": "Excellent performance expected. Player shows strong attacking threat and good physical condition.",
  "strengths": [
    "Strong attacking (goals + assists)",
    "Good ball retention",
    "High physical intensity"
  ],
  "weaknesses": [
    "Room for defensive improvement"
  ],
  "confidence": "HIGH",
  "algorithm": "Gradient Boosting Regressor"
}
```

**How to Interpret:**
```
Performance Category Legend:
  ★★★★★ LEGEND      → 95-100 (Exceptional performance)
  ★★★★☆ EXCELLENT  → 85-94  (Outstanding performance)
  ★★★☆☆ GOOD       → 70-84  (Strong performance)
  ★★☆☆☆ AVERAGE    → 50-69  (Adequate performance)
  ★☆☆☆☆ BAD        → 30-49  (Below par)
  ☆☆☆☆☆ VERY_BAD   → <30    (Poor performance)
```

---

### Test 1.2: Batch Prediction (Testing Multiple Players)

**Use Case:** Predict performance for entire squad

```bash
curl -X POST http://localhost:8080/api/players/ai-predict-batch \
  -H "Content-Type: application/json" \
  -d '[
    {
      "playerId": 1,
      "goals": 3,
      "assists": 2,
      "tackles": 5,
      "interceptions": 3,
      "passesCompleted": 52,
      "passAccuracy": 87.5,
      "distanceCoveredKm": 10.5,
      "averageSpeedKmh": 25.8,
      "ballPossessionPercent": 51.0,
      "foulsCommitted": 1,
      "shotsOnTarget": 4
    },
    {
      "playerId": 2,
      "goals": 1,
      "assists": 0,
      "tackles": 8,
      "interceptions": 6,
      "passesCompleted": 56,
      "passAccuracy": 90.2,
      "distanceCoveredKm": 11.2,
      "averageSpeedKmh": 26.5,
      "ballPossessionPercent": 48.0,
      "foulsCommitted": 3,
      "shotsOnTarget": 1
    }
  ]'
```

**Expected Response:**
```json
[
  {
    "playerId": 1,
    "predictedPerformanceRating": 76.5,
    "performanceCategory": "EXCELLENT",
    "confidence": "HIGH"
  },
  {
    "playerId": 2,
    "predictedPerformanceRating": 71.2,
    "performanceCategory": "GOOD",
    "confidence": "HIGH"
  }
]
```

---

### Test 1.3: AI Service Availability Check

**Use Case:** Verify AI service is loaded and ready

```bash
curl http://localhost:5000/api/ai/health
```

**Expected Response:**
```json
{
  "status": "ok",
  "exercise_model_loaded": true,
  "player_prediction_model_loaded": true,
  "timestamp": "2026-05-15T10:30:45"
}
```

---

## 🎯 FEATURE 2: Advanced Métier 1 - Basic Performance Prediction

### Overview
- **What:** Predicts player performance using historical data
- **How:** Linear regression + weighted recent form analysis
- **Data Source:** Player's match history in database
- **Confidence:** Medium (based on number of historical matches)

### Algorithm Logic

```
Predicted_Rating = (
    Average_Historical_Rating * 0.4 +
    Recent_Form_Weight * 0.3 +
    Trend_Direction_Boost * 0.2 +
    Position_Average * 0.1
)

Recent_Form_Weight = Average of last 3-5 matches (recent = higher impact)
Trend_Direction_Boost = +5 if improving, -5 if declining
```

---

### Test 2.1: Get Basic Prediction for Existing Player

**Use Case:** Predict performance for database player

```bash
# Get prediction for player ID 1 (assuming exists in database)
curl http://localhost:8080/api/players/1/prediction
```

**Expected Response:**
```json
{
  "status": "ok",
  "type": "BASIC_PREDICTION",
  "algorithm": "Linear Regression with Weighted Recent Form",
  "prediction": {
    "playerId": 1,
    "predictedPerformanceRating": 72.5,
    "predictedGoals": 2.1,
    "predictedAssists": 0.8,
    "predictedTackles": 5.3,
    "predictedInterceptions": 2.9,
    "predictedPassAccuracy": 85.2,
    "performanceCategory": "GOOD",
    "reliability": 85.0,
    "trendDirection": "IMPROVING",
    "totalMatchesAnalyzed": 15,
    "averageHistoricalRating": 71.2,
    "recentFormRating": 74.8
  }
}
```

**What to Check:**
```
✓ Reliability > 70%          = Enough match history
✓ Trend Direction            = Improving/Stable/Declining
✓ Recent > Historical        = Player improving (trendDirection = IMPROVING)
✓ Predicted ≈ Recent Form    = Uses recent form weighting
```

---

### Test 2.2: Compare Multiple Players

**Use Case:** Analyze entire team's form

```bash
# Get predictions for players 1-5
for i in {1..5}; do
  echo "=== Player $i ==="
  curl http://localhost:8080/api/players/$i/prediction
done
```

**Expected Output Pattern:**
```
=== Player 1 ===
Performance: 72.5 (GOOD), Trend: IMPROVING
=== Player 2 ===
Performance: 65.3 (AVERAGE), Trend: DECLINING
=== Player 3 ===
Performance: 78.9 (EXCELLENT), Trend: STABLE
...
```

---

## 🎯 FEATURE 3: Advanced Métier 2 - Advanced AI Performance Prediction

### Overview
- **What:** Advanced AI prediction using ML ensemble
- **How:** Calls Python service + returns comparison with basic prediction
- **Data Source:** Provided player statistics
- **Advantages:** 17 features vs basic 4-5 metrics; Non-linear relationships

---

### Test 3.1: Get Both Predictions (AI + Basic Comparison)

**Use Case:** Full analysis - AI vs Traditional approach

```bash
curl http://localhost:8080/api/players/1/ai-prediction
```

**Expected Response:**
```json
{
  "status": "ok",
  "type": "AI_PREDICTION",
  "message": "Comparison: Basic vs Advanced AI Prediction",
  "basicPrediction": {
    "playerId": 1,
    "predictedPerformanceRating": 72.5,
    "performanceCategory": "GOOD",
    "reliability": 85.0,
    "trendDirection": "IMPROVING",
    "algorithm": "Linear Regression"
  },
  "aiPrediction": {
    "playerId": 1,
    "predictedPerformanceRating": 76.5,
    "performanceCategory": "EXCELLENT",
    "interpretation": "Excellent performance expected",
    "strengths": ["Attack", "Endurance"],
    "weaknesses": ["Discipline"],
    "confidence": "HIGH",
    "algorithm": "Gradient Boosting Regressor"
  },
  "comparison": {
    "ratingDifference": 4.0,
    "categoryDifference": "GOOD → EXCELLENT",
    "recommendation": "AI model predicts 4 points higher due to engineered features"
  }
}
```

**Why Different?**
```
Basic Prediction (72.5):
- Uses only historical matches
- Simple linear regression
- 4-5 features (goals, assists, etc.)

AI Prediction (76.5):
- Uses 17 engineered features
- Captures non-linear relationships
- Considers discipline factor, physical intensity
- Higher accuracy (R² = 0.88)
```

---

### Test 3.2: Analyze Prediction Differences

**Scenario:** When AI and Basic predictions differ significantly

```bash
# Test with an improving player
curl -X POST http://localhost:8080/api/players/ai-predict-custom \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": 50,
    "goals": 4,
    "assists": 3,
    "tackles": 2,
    "interceptions": 1,
    "passesCompleted": 50,
    "passAccuracy": 86.5,
    "distanceCoveredKm": 10.8,
    "averageSpeedKmh": 26.3,
    "ballPossessionPercent": 52.0,
    "foulsCommitted": 0,
    "shotsOnTarget": 6
  }'
```

**Expected High Rating (85+):**
```
Why?
- 4 goals (good attacking)
- 3 assists (good playmaking)
- 50 pass accuracy (good distribution)
- 0 fouls (perfect discipline)
- 6 shots on target (aggressive)

Result: ~85 rating (EXCELLENT)
```

---

## 🖥️ BROWSER CONSOLE TESTING (No Postman Needed)

### Test in Browser DevTools

1. Open **Chrome/Firefox DevTools** (`F12`)
2. Go to **Console** tab
3. Paste these commands:

### Test 1: Check Basic Prediction

```javascript
// Get player 1 basic prediction
fetch('http://localhost:8080/api/players/1/prediction')
  .then(r => r.json())
  .then(d => console.table(d.prediction))
```

**Output:**
```
┌─────────────────────────────┬────────┐
│ playerId                    │ 1      │
│ predictedPerformanceRating  │ 72.5   │
│ performanceCategory         │ GOOD   │
│ reliability                 │ 85     │
│ trendDirection              │ IMPROVING │
└─────────────────────────────┴────────┘
```

### Test 2: Get AI Prediction with Comparison

```javascript
// Get AI prediction with comparison
fetch('http://localhost:8080/api/players/1/ai-prediction')
  .then(r => r.json())
  .then(d => {
    console.log('=== BASIC PREDICTION ===');
    console.table(d.basicPrediction);
    console.log('=== AI PREDICTION ===');
    console.table(d.aiPrediction);
    console.log('=== COMPARISON ===');
    console.log(d.comparison);
  })
```

### Test 3: Batch Predict Multiple Players

```javascript
// Predict for 3 players at once
const players = [
  {
    "playerId": 1,
    "goals": 3, "assists": 2, "tackles": 5, "interceptions": 3,
    "passesCompleted": 52, "passAccuracy": 87.5,
    "distanceCoveredKm": 10.5, "averageSpeedKmh": 25.8,
    "ballPossessionPercent": 51.0, "foulsCommitted": 1, "shotsOnTarget": 4
  },
  {
    "playerId": 2,
    "goals": 1, "assists": 0, "tackles": 8, "interceptions": 6,
    "passesCompleted": 56, "passAccuracy": 90.2,
    "distanceCoveredKm": 11.2, "averageSpeedKmh": 26.5,
    "ballPossessionPercent": 48.0, "foulsCommitted": 3, "shotsOnTarget": 1
  },
  {
    "playerId": 3,
    "goals": 2, "assists": 1, "tackles": 4, "interceptions": 2,
    "passesCompleted": 49, "passAccuracy": 84.3,
    "distanceCoveredKm": 9.8, "averageSpeedKmh": 24.5,
    "ballPossessionPercent": 50.0, "foulsCommitted": 2, "shotsOnTarget": 3
  }
];

fetch('http://localhost:8080/api/players/ai-predict-batch', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(players)
})
  .then(r => r.json())
  .then(d => console.table(d))
```

**Output:**
```
┌──────────┬──────────────────────────┬──────────────────────┐
│ playerId │ predictedPerformanceRating│ performanceCategory  │
├──────────┼──────────────────────────┼──────────────────────┤
│ 1        │ 76.5                     │ EXCELLENT            │
│ 2        │ 71.2                     │ GOOD                 │
│ 3        │ 68.9                     │ AVERAGE              │
└──────────┴──────────────────────────┴──────────────────────┘
```

---

## 📊 TEST DATA SCENARIOS

### Scenario A: Strong Attacker

```json
{
  "playerId": 101,
  "goals": 5,
  "assists": 3,
  "tackles": 1,
  "interceptions": 0,
  "passesCompleted": 45,
  "passAccuracy": 82.0,
  "distanceCoveredKm": 9.5,
  "averageSpeedKmh": 24.0,
  "ballPossessionPercent": 48.0,
  "foulsCommitted": 2,
  "shotsOnTarget": 7
}
```

**Expected:** High rating (80+), strengths = attacking/shots

---

### Scenario B: Defensive Midfielder

```json
{
  "playerId": 102,
  "goals": 0,
  "assists": 1,
  "tackles": 9,
  "interceptions": 7,
  "passesCompleted": 58,
  "passAccuracy": 91.5,
  "distanceCoveredKm": 11.8,
  "averageSpeedKmh": 26.5,
  "ballPossessionPercent": 52.0,
  "foulsCommitted": 1,
  "shotsOnTarget": 0
}
```

**Expected:** Good rating (72+), strengths = defense/passes

---

### Scenario C: Poor Performance (Injured/Tired)

```json
{
  "playerId": 103,
  "goals": 0,
  "assists": 0,
  "tackles": 2,
  "interceptions": 1,
  "passesCompleted": 35,
  "passAccuracy": 70.0,
  "distanceCoveredKm": 7.2,
  "averageSpeedKmh": 20.5,
  "ballPossessionPercent": 38.0,
  "foulsCommitted": 4,
  "shotsOnTarget": 0
}
```

**Expected:** Low rating (<50), weaknesses = all metrics

---

## 🐛 DEBUG & TROUBLESHOOTING

### Issue 1: "Model not loaded" Error

**Symptom:**
```json
{
  "error": "Model not loaded or AI service unavailable"
}
```

**Solution:**
```bash
# 1. Check if Python service is running
curl http://localhost:5000/api/ai/health

# 2. If not running, start it
cd d:\PI2027\PI_StreetLeague\ai-service
python app.py

# 3. If running but model not loaded, retrain
python train_player_prediction_model.py

# 4. Check model files exist
ls -la d:\PI2027\PI_StreetLeague\ai-service\model\
```

---

### Issue 2: "Port 5000/8080 already in use"

**Solution:**
```bash
# Kill process using port 5000 (Python)
lsof -i :5000
kill -9 <PID>

# Kill process using port 8080 (Java)
lsof -i :8080
kill -9 <PID>

# Or restart terminal
```

---

### Issue 3: Different Results on Each Test

**Expected Behavior:**
- ✅ Same input = Same output (models are deterministic)
- ✅ Different input = Different output
- ✅ Slight floating-point differences OK (< 0.1)

**If Results Vary Wildly:**
```bash
# Retrain models (old model might be incompatible)
python train_player_prediction_model.py
```

---

### Issue 4: CSV Not Found Error

**Symptom:**
```
[Training] CSV not found at data/player_performance_dataset.csv
[Training] Generating synthetic training data...
```

**Solution:**
```bash
# Verify CSV exists
cat d:\PI2027\PI_StreetLeague\ai-service\data\player_performance_dataset.csv

# If missing, check we created it in ai-service/data directory
# It should be in: d:\PI2027\PI_StreetLeague\ai-service\data\player_performance_dataset.csv
```

---

## ✅ TESTING CHECKLIST

```
PRE-TESTING:
□ Python service running on :5000
□ Java backend running on :8080
□ Both services respond to health check
□ player_performance_dataset.csv exists in data/ folder

FEATURE 1 - Future Player Performance:
□ Custom prediction works with test data
□ Returns 0-100 rating
□ Returns performance category
□ Returns strengths/weaknesses
□ Batch prediction works (multiple players)
□ Confidence level is HIGH/MEDIUM/LOW

FEATURE 2 - Advanced Métier 1 (Basic):
□ Get prediction for player 1 works
□ Returns reliability metric
□ Returns trend direction
□ Trend = IMPROVING/STABLE/DECLINING
□ Rating based on historical data

FEATURE 3 - Advanced Métier 2 (Advanced AI):
□ Compare AI vs Basic prediction works
□ AI rating sometimes differs from Basic
□ Difference explained in comparison
□ AI confidence is HIGH

BROWSER TESTING:
□ Console fetch() for prediction works
□ console.table() displays results nicely
□ Batch prediction in console works

EDGE CASES:
□ Test with all zeros (should be low)
□ Test with all max values (should be high)
□ Test with negative fouls (should be invalid)
□ Test with accuracy > 100 (should be capped)
```

---

## 📈 SUCCESS CRITERIA

| Feature | Success Metric |
|---------|----------------|
| **Future Player Performance** | Returns rating 0-100 with HIGH confidence |
| **Advanced Métier 1** | Returns trend direction + reliability > 70% |
| **Advanced Métier 2** | AI rating ≠ Basic rating (shows added value) |

---

## 🎯 EXAMPLE TEST SUITE (Copy-Paste Ready)

```bash
#!/bin/bash
# Save as: test_all_features.sh

echo "=== TESTING FUTURE PLAYER PERFORMANCE PREDICTION ==="

echo "Test 1: Custom prediction"
curl -X POST http://localhost:8080/api/players/ai-predict-custom \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": 101, "goals": 3, "assists": 2, "tackles": 5,
    "interceptions": 3, "passesCompleted": 52, "passAccuracy": 87.5,
    "distanceCoveredKm": 10.5, "averageSpeedKmh": 25.8,
    "ballPossessionPercent": 51.0, "foulsCommitted": 1, "shotsOnTarget": 4
  }' | python -m json.tool

echo -e "\n\n=== TESTING ADVANCED MÉTIER 1 (BASIC PREDICTION) ==="

echo "Test 2: Get basic prediction"
curl http://localhost:8080/api/players/1/prediction | python -m json.tool

echo -e "\n\n=== TESTING ADVANCED MÉTIER 2 (ADVANCED AI PREDICTION) ==="

echo "Test 3: Get AI prediction with comparison"
curl http://localhost:8080/api/players/1/ai-prediction | python -m json.tool

echo -e "\n✅ All tests completed!"
```

---

## 📞 QUICK REFERENCE URLs

```
Health Checks:
  Python:  http://localhost:5000/api/ai/health
  Java:    http://localhost:8080/api/players/ai/health

Player Performance Prediction (Feature 1):
  Custom:  POST /api/players/ai-predict-custom
  Batch:   POST /api/players/ai-predict-batch
  Health:  GET  /api/players/ai/health

Basic Prediction (Feature 2 - Advanced Métier 1):
  Get:     GET  /api/players/{playerId}/prediction

Advanced AI Prediction (Feature 3 - Advanced Métier 2):
  Compare: GET  /api/players/{playerId}/ai-prediction
```

---

**You're ready to test! Start with Setup (Section 1), then work through Features 1-3.** ✅
