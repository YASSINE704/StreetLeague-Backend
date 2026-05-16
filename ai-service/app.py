"""
StreetLeague AI Service - Recommandation d'Exercices & Prédiction de Performance
==================================================================================
Ce service Flask charge les modèles ML entraînés et expose une API REST pour :
1. Recommander des exercices (Intelligent Exercise Recommendation System)
2. Prédire la performance des joueurs (Future Player Performance Prediction)

Architecture :
  - train_model.py & train_player_prediction_model.py entraînent les modèles et les exportent dans model/
  - Ce fichier (app.py) charge les modèles exportés et sert les prédictions
  - Spring Boot appelle ce service via REST

Endpoints :
  - POST /api/ai/recommend - Recommandations d'exercices
  - POST /api/ai/predict-player-performance - Prédictions de performance
  - GET /api/ai/health - Health check

Lancer les notebooks AVANT de démarrer ce service pour générer les modèles.
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import numpy as np
import joblib
import os
from scipy.sparse import hstack, csr_matrix

app = Flask(__name__)
CORS(app)

# ============================================================
# Chargement des modèles entraînés
# ============================================================
MODEL_DIR = os.path.join(os.path.dirname(__file__), 'model')
DATA_DIR = os.path.join(os.path.dirname(__file__), 'data')

# Exercise Recommendation Model
exercise_model_loaded = False
knn_model = None
tfidf = None
scaler = None
df = None
combined_features = None

# Player Performance Prediction Model
player_pred_model_loaded = False
player_prediction_model = None
player_prediction_scaler = None
player_prediction_features = None
player_prediction_metadata = None


def load_model():
    """Charge les modèles depuis les fichiers exportés par les notebooks."""
    global exercise_model_loaded, knn_model, tfidf, scaler, df, combined_features
    global player_pred_model_loaded, player_prediction_model, player_prediction_scaler, player_prediction_features, player_prediction_metadata

    # ========== Charger le modèle de recommandation d'exercices ==========
    try:
        knn_model = joblib.load(os.path.join(MODEL_DIR, 'knn_model.joblib'))
        tfidf = joblib.load(os.path.join(MODEL_DIR, 'tfidf_vectorizer.joblib'))
        scaler = joblib.load(os.path.join(MODEL_DIR, 'scaler.joblib'))
        combined_features = joblib.load(os.path.join(MODEL_DIR, 'combined_features.joblib'))
        df = pd.read_csv(os.path.join(MODEL_DIR, 'exercises_enriched.csv'))
        exercise_model_loaded = True
        print("[AI Service] ✓ Modèle de recommandation d'exercices chargé avec succès")
    except FileNotFoundError as e:
        print(f"[AI Service] ATTENTION: Modèle d'exercices non trouvé ({e}).")
        print("             Exécutez train_model.py d'abord. Mode fallback pour les exercices.")
        exercise_model_loaded = False
        try:
            df = pd.read_csv(os.path.join(DATA_DIR, 'exercises_dataset.csv'))
            print("[AI Service] Dataset d'exercices brut chargé pour le mode fallback.")
        except Exception:
            df = None

    # ========== Charger le modèle de prédiction de performance ==========
    try:
        player_prediction_model = joblib.load(os.path.join(MODEL_DIR, 'player_performance_model.joblib'))
        player_prediction_scaler = joblib.load(os.path.join(MODEL_DIR, 'player_performance_scaler.joblib'))
        player_prediction_features = joblib.load(os.path.join(MODEL_DIR, 'player_performance_feature_names.joblib'))
        player_prediction_metadata = joblib.load(os.path.join(MODEL_DIR, 'player_performance_metadata.joblib'))
        player_pred_model_loaded = True
        print("[AI Service] ✓ Modèle de prédiction de performance chargé avec succès")
    except FileNotFoundError as e:
        print(f"[AI Service] ATTENTION: Modèle de prédiction non trouvé ({e}).")
        print("             Exécutez train_player_prediction_model.py d'abord.")
        player_pred_model_loaded = False


def recommend_with_model(context, top_n=6):
    """
    Recommandation via le modèle KNN entraîné.
    Utilise TF-IDF + features numériques + cosine similarity.
    """
    # Construire le texte de requête
    query_parts = []
    if 'typeSeance' in context:
        query_parts.append(str(context['typeSeance']))
    if 'intensite' in context:
        query_parts.append(str(context['intensite']))
    if 'objectifProgramme' in context:
        query_parts.append(str(context['objectifProgramme']))
    if 'niveauJoueurs' in context:
        query_parts.append(str(context['niveauJoueurs']))

    query_text = ' '.join(query_parts) if query_parts else 'FORCE INTERMEDIAIRE'

    # Vectoriser la requête avec TF-IDF
    query_tfidf = tfidf.transform([query_text])

    # Mapper les features numériques du contexte
    intensite_map = {
        'FAIBLE': 3, 'MODEREE': 5, 'MOYENNE': 5, 'MODERE': 5,
        'ELEVEE': 8, 'FORTE': 8, 'HAUTE': 8, 'INTENSE': 9
    }
    type_map = {'FORCE': 1, 'CARDIO': 0, 'MOBILITE': 2, 'TECHNIQUE': 3}
    diff_map = {'DEBUTANT': 1, 'INTERMEDIAIRE': 2, 'AVANCE': 0}

    intensite_val = intensite_map.get(str(context.get('intensite', 'MOYENNE')).upper(), 5)
    type_val = type_map.get(str(context.get('typeSeance', 'FORCE')).upper(), 0)
    diff_val = diff_map.get(str(context.get('niveauJoueurs', 'INTERMEDIAIRE')).upper(), 1)
    duree_val = context.get('dureeSeanceMinutes', 60) / 60.0 * 20
    calories_val = intensite_val * 20

    query_numeric = scaler.transform([[intensite_val, duree_val, calories_val, type_val, diff_val]])
    query_combined = hstack([query_tfidf, csr_matrix(query_numeric)])

    # Trouver les K plus proches voisins
    distances, indices = knn_model.kneighbors(query_combined, n_neighbors=min(top_n, len(df)))

    # Construire les recommandations
    recommendations = []
    for i, (idx, dist) in enumerate(zip(indices[0], distances[0])):
        row = df.iloc[idx]
        score = round((1 - dist) * 100, 1)  # Convertir distance cosinus en score %
        recommendations.append({
            "nom": row['nom'],
            "type": row['type'],
            "description": row.get('objectif', ''),
            "difficulte": row['difficulte'],
            "dureeMinutes": int(row['dureeMinutes']),
            "equipement": row['equipement'],
            "objectif": row['objectif'],
            "niveauRecommande": row['niveauRecommande'],
            "consigneSecurite": row['consigneSecurite'],
            "scoreRelevance": score,
            "raison": build_reason(row, context, score)
        })

    return recommendations


def recommend_fallback(context, top_n=6):
    """
    Mode fallback : scoring simple quand le modèle n'est pas disponible.
    Utilisé si le notebook n'a pas encore été exécuté.
    """
    if df is None:
        return []

    scored = []
    for _, row in df.iterrows():
        score = 0
        # Type match
        if str(row.get('type', '')).upper() == str(context.get('typeSeance', '')).upper():
            score += 30
        # Intensité match
        intensite_mapping = {
            'FAIBLE': 'DEBUTANT', 'MODEREE': 'INTERMEDIAIRE', 'MODERE': 'INTERMEDIAIRE',
            'MOYENNE': 'INTERMEDIAIRE', 'ELEVEE': 'AVANCE', 'FORTE': 'AVANCE', 'INTENSE': 'AVANCE'
        }
        mapped = intensite_mapping.get(str(context.get('intensite', '')).upper(), '')
        if mapped == str(row.get('difficulte', '')).upper():
            score += 20
        # Objectif keywords
        objectif = str(context.get('objectifProgramme', '')).lower()
        row_obj = str(row.get('objectif', '')).lower()
        for kw in objectif.split():
            if len(kw) > 3 and kw in row_obj:
                score += 15
                break
        # Pas d'équipement
        if str(row.get('equipement', '')).lower() in ['aucun', 'none', '']:
            score += 10

        scored.append({
            "nom": row['nom'],
            "type": row['type'],
            "description": row.get('objectif', ''),
            "difficulte": row['difficulte'],
            "dureeMinutes": int(row['dureeMinutes']),
            "equipement": row['equipement'],
            "objectif": row.get('objectif', ''),
            "niveauRecommande": row.get('niveauRecommande', ''),
            "consigneSecurite": row.get('consigneSecurite', ''),
            "scoreRelevance": score,
            "raison": f"Score de pertinence : {score}/75 (mode fallback)"
        })

    scored.sort(key=lambda x: x['scoreRelevance'], reverse=True)
    return scored[:top_n]


def build_reason(row, context, score):
    """Construit une explication de la recommandation."""
    raisons = []
    type_seance = str(context.get('typeSeance', '')).upper()

    if str(row['type']).upper() == type_seance:
        raisons.append(f"correspond au type de séance ({type_seance})")

    intensite_mapping = {
        'FAIBLE': 'DEBUTANT', 'MODEREE': 'INTERMEDIAIRE', 'MODERE': 'INTERMEDIAIRE',
        'MOYENNE': 'INTERMEDIAIRE', 'ELEVEE': 'AVANCE', 'FORTE': 'AVANCE'
    }
    mapped = intensite_mapping.get(str(context.get('intensite', '')).upper(), '')
    if mapped == str(row.get('difficulte', '')).upper():
        raisons.append("intensité adaptée au niveau")

    objectif = str(context.get('objectifProgramme', '')).lower()
    row_obj = str(row.get('objectif', '')).lower()
    for kw in objectif.split():
        if len(kw) > 3 and kw in row_obj:
            raisons.append(f"correspond à l'objectif ({kw})")
            break

    if str(row.get('equipement', '')).lower() in ['aucun', 'none', '']:
        raisons.append("ne nécessite pas d'équipement")

    if not raisons:
        raisons.append("exercice complémentaire recommandé par le modèle")

    return f"Recommandé (score: {score}%) — " + ", ".join(raisons)


# ============================================================
# API Endpoints - Recommandations d'Exercices
# ============================================================

@app.route("/api/ai/health", methods=["GET"])
def health():
    """Health check — indique si les modèles ML sont chargés."""
    return jsonify({
        "status": "ok",
        "exercise_model_loaded": exercise_model_loaded,
        "player_prediction_model_loaded": player_pred_model_loaded,
        "exercise_mode": "ml_model" if exercise_model_loaded else "fallback",
        "prediction_mode": "ml_model" if player_pred_model_loaded else "unavailable",
        "dataset_size": len(df) if df is not None else 0
    }), 200


@app.route("/api/ai/recommend", methods=["POST"])
def recommend():
    """
    Endpoint principal de recommandation d'exercices.
    Reçoit un contexte JSON et retourne 6 exercices recommandés.

    Input JSON:
    {
        "typeSeance": "CARDIO",
        "intensite": "FORTE",
        "objectifProgramme": "endurance vitesse",
        "niveauJoueurs": "INTERMEDIAIRE",
        "dureeSeanceMinutes": 60,
        "nbParticipants": 5
    }
    """
    context = request.get_json()
    if not context:
        return jsonify({
            "status": "error",
            "message": "Contexte JSON requis",
            "nbRecommandations": 0,
            "recommandations": []
        }), 400

    # Utiliser le modèle ML si disponible, sinon fallback
    if exercise_model_loaded:
        recommendations = recommend_with_model(context, top_n=6)
        mode = "ml_model"
    else:
        recommendations = recommend_fallback(context, top_n=6)
        mode = "fallback"

    return jsonify({
        "status": "ok",
        "mode": mode,
        "message": "Recommandations générées avec succès"
                   + (" (modèle ML)" if exercise_model_loaded else " (mode fallback — exécutez le notebook)"),
        "nbRecommandations": len(recommendations),
        "recommandations": recommendations
    }), 200


@app.route("/api/ai/model-info", methods=["GET"])
def model_info():
    """Informations sur le modèle chargé."""
    info = {
        "exercise_model_loaded": exercise_model_loaded,
        "player_prediction_model_loaded": player_pred_model_loaded,
        "exercise_algorithm": "KNN (K=6) + TF-IDF + Cosine Similarity" if exercise_model_loaded else "Scoring heuristique (fallback)",
        "prediction_algorithm": player_prediction_metadata.get('model_type', 'Unknown') if player_pred_model_loaded else "N/A",
        "dataset_size": len(df) if df is not None else 0,
        "features": "TF-IDF (texte) + intensite_score + dureeMinutes + calories + type + difficulte" if exercise_model_loaded else "N/A",
        "metric": "cosine" if exercise_model_loaded else "N/A"
    }
    return jsonify(info), 200


# ============================================================
# API Endpoints - Prédiction de Performance Joueur
# ============================================================

def predict_player_performance(player_stats):
    """
    Prédit la performance d'un joueur basée sur ses statistiques.
    
    Input: {
        "goals": 2,
        "assists": 1,
        "tackles": 5,
        "interceptions": 3,
        "passes_completed": 45,
        "pass_accuracy": 82.5,
        "distance_covered_km": 10.2,
        "average_speed_kmh": 25.1,
        "ball_possession_percent": 55,
        "fouls_committed": 2,
        "shots_on_target": 3
    }
    
    Returns: Predicted performance rating (0-100)
    """
    if not player_pred_model_loaded:
        return None
    
    try:
        # Préparer les features dans le même ordre que l'entraînement
        feature_values = []
        for feature in player_prediction_features:
            value = player_stats.get(feature, 0)
            feature_values.append(value)
        
        # Convertir en array numpy
        feature_array = np.array([feature_values])
        
        # Normaliser les features
        feature_array_scaled = player_prediction_scaler.transform(feature_array)
        
        # Prédire
        prediction = player_prediction_model.predict(feature_array_scaled)[0]
        
        # Cliper la prédiction entre 0 et 100
        prediction = np.clip(prediction, 0, 100)
        
        return round(prediction, 2)
    except Exception as e:
        print(f"[AI Service] Erreur lors de la prédiction : {e}")
        return None


def build_prediction_response(player_id, player_stats, predicted_rating):
    """Construit une réponse de prédiction avec explications."""
    
    # Catégories de performance
    if predicted_rating <= 20:
        category = "VERY_BAD"
        interpretation = "Performance attendue très faible"
    elif predicted_rating <= 40:
        category = "BAD"
        interpretation = "Performance attendue faible"
    elif predicted_rating <= 60:
        category = "AVERAGE"
        interpretation = "Performance attendue moyenne"
    elif predicted_rating <= 75:
        category = "GOOD"
        interpretation = "Performance attendue bonne"
    elif predicted_rating <= 85:
        category = "EXCELLENT"
        interpretation = "Performance attendue excellente"
    else:
        category = "LEGEND"
        interpretation = "Performance attendue légendaire"
    
    # Identifier les forces et faiblesses
    strengths = []
    weaknesses = []
    
    if player_stats.get('goals', 0) > 2:
        strengths.append("Attaque (buts)")
    if player_stats.get('pass_accuracy', 0) > 80:
        strengths.append("Précision de passes")
    if player_stats.get('tackles', 0) > 5:
        strengths.append("Défense (tacles)")
    if player_stats.get('distance_covered_km', 0) > 10:
        strengths.append("Endurance")
    
    if player_stats.get('fouls_committed', 0) > 3:
        weaknesses.append("Discipline (fautes)")
    if player_stats.get('pass_accuracy', 0) < 70:
        weaknesses.append("Précision")
    if player_stats.get('tackles', 0) < 2:
        weaknesses.append("Engagement défensif")
    
    return {
        "player_id": player_id,
        "predicted_performance_rating": predicted_rating,
        "performance_category": category,
        "interpretation": interpretation,
        "strengths": strengths if strengths else ["Équilibré"],
        "weaknesses": weaknesses if weaknesses else ["Aucune faiblesse notable"],
        "confidence": "HIGH" if player_pred_model_loaded else "LOW",
        "algorithm": player_prediction_metadata.get('model_type', 'Unknown') if player_pred_model_loaded else "N/A"
    }


@app.route("/api/ai/predict-player-performance", methods=["POST"])
def predict_player():
    """
    Endpoint de prédiction de performance joueur.
    Reçoit les statistiques du joueur et retourne une prédiction de performance.
    
    Input JSON:
    {
        "player_id": 1,
        "goals": 2,
        "assists": 1,
        "tackles": 5,
        "interceptions": 3,
        "passes_completed": 45,
        "pass_accuracy": 82.5,
        "distance_covered_km": 10.2,
        "average_speed_kmh": 25.1,
        "ball_possession_percent": 55,
        "fouls_committed": 2,
        "shots_on_target": 3
    }
    """
    data = request.get_json()
    if not data:
        return jsonify({
            "status": "error",
            "message": "Données JSON requises"
        }), 400
    
    player_id = data.get('player_id')
    if not player_id:
        return jsonify({
            "status": "error",
            "message": "player_id requis"
        }), 400
    
    if not player_pred_model_loaded:
        return jsonify({
            "status": "error",
            "message": "Modèle de prédiction non disponible",
            "hint": "Exécutez train_player_prediction_model.py"
        }), 503
    
    # Extraire les stats du joueur
    player_stats = {
        'goals': data.get('goals', 0),
        'assists': data.get('assists', 0),
        'tackles': data.get('tackles', 0),
        'interceptions': data.get('interceptions', 0),
        'passes_completed': data.get('passes_completed', 0),
        'pass_accuracy': data.get('pass_accuracy', 75),
        'distance_covered_km': data.get('distance_covered_km', 10),
        'average_speed_kmh': data.get('average_speed_kmh', 25),
        'ball_possession_percent': data.get('ball_possession_percent', 50),
        'fouls_committed': data.get('fouls_committed', 0),
        'shots_on_target': data.get('shots_on_target', 0),
    }
    
    # Prédire
    predicted_rating = predict_player_performance(player_stats)
    
    if predicted_rating is None:
        return jsonify({
            "status": "error",
            "message": "Erreur lors de la prédiction"
        }), 500
    
    # Construire la réponse
    response = build_prediction_response(player_id, player_stats, predicted_rating)
    
    return jsonify({
        "status": "ok",
        "mode": "ml_model",
        "prediction": response
    }), 200


@app.route("/api/ai/predict-batch", methods=["POST"])
def predict_batch():
    """
    Endpoint pour prédictions par lots (multiple joueurs).
    
    Input JSON:
    {
        "players": [
            {"player_id": 1, "goals": 2, "assists": 1, ...},
            {"player_id": 2, "goals": 3, "assists": 2, ...}
        ]
    }
    """
    data = request.get_json()
    if not data or 'players' not in data:
        return jsonify({
            "status": "error",
            "message": "Format attendu: {\"players\": [...]}"
        }), 400
    
    if not player_pred_model_loaded:
        return jsonify({
            "status": "error",
            "message": "Modèle de prédiction non disponible"
        }), 503
    
    players = data.get('players', [])
    predictions = []
    
    for player_data in players:
        player_id = player_data.get('player_id')
        if not player_id:
            continue
        
        player_stats = {
            'goals': player_data.get('goals', 0),
            'assists': player_data.get('assists', 0),
            'tackles': player_data.get('tackles', 0),
            'interceptions': player_data.get('interceptions', 0),
            'passes_completed': player_data.get('passes_completed', 0),
            'pass_accuracy': player_data.get('pass_accuracy', 75),
            'distance_covered_km': player_data.get('distance_covered_km', 10),
            'average_speed_kmh': player_data.get('average_speed_kmh', 25),
            'ball_possession_percent': player_data.get('ball_possession_percent', 50),
            'fouls_committed': player_data.get('fouls_committed', 0),
            'shots_on_target': player_data.get('shots_on_target', 0),
        }
        
        predicted_rating = predict_player_performance(player_stats)
        if predicted_rating is not None:
            response = build_prediction_response(player_id, player_stats, predicted_rating)
            predictions.append(response)
    
    return jsonify({
        "status": "ok",
        "mode": "ml_model",
        "total": len(predictions),
        "predictions": predictions
    }), 200


# ============================================================
# Démarrage
# ============================================================
if __name__ == "__main__":
    load_model()
    print("\n" + "="*80)
    print("[AI Service] Démarrage sur http://localhost:5000")
    print("="*80)
    print(f"[AI Service] Modèle Exercices       : {'✓ Chargé (ML Mode)' if exercise_model_loaded else '✗ Non disponible (Mode Fallback)'}")
    print(f"[AI Service] Modèle Prédictions    : {'✓ Chargé (ML Mode)' if player_pred_model_loaded else '✗ Non disponible'}")
    print("\n[API Endpoints]")
    print("  - GET  /api/ai/health                          : Health check")
    print("  - POST /api/ai/recommend                       : Recommandation d'exercices")
    print("  - POST /api/ai/predict-player-performance      : Prédiction performance joueur")
    print("  - POST /api/ai/predict-batch                   : Prédictions par lots")
    print("  - GET  /api/ai/model-info                      : Info modèles")
    print("="*80 + "\n")
    app.run(host="0.0.0.0", port=5000, debug=False)
