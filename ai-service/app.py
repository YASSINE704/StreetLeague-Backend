"""
StreetLeague AI Service - Recommandation d'Exercices
=====================================================
Ce service Flask charge le modèle ML entraîné dans le notebook
et expose une API REST pour recommander des exercices.

Architecture :
  - Le notebook (notebook.ipynb) entraîne le modèle et l'exporte dans model/
  - Ce fichier (app.py) charge le modèle exporté et sert les prédictions
  - Spring Boot appelle ce service via REST (POST /api/ai/recommend)

Lancer le notebook AVANT de démarrer ce service pour générer le modèle.
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
# Chargement du modèle entraîné
# ============================================================
MODEL_DIR = os.path.join(os.path.dirname(__file__), 'model')
DATA_DIR = os.path.join(os.path.dirname(__file__), 'data')

model_loaded = False
knn_model = None
tfidf = None
scaler = None
df = None
combined_features = None


def load_model():
    """Charge le modèle depuis les fichiers exportés par le notebook."""
    global model_loaded, knn_model, tfidf, scaler, df, combined_features

    try:
        knn_model = joblib.load(os.path.join(MODEL_DIR, 'knn_model.joblib'))
        tfidf = joblib.load(os.path.join(MODEL_DIR, 'tfidf_vectorizer.joblib'))
        scaler = joblib.load(os.path.join(MODEL_DIR, 'scaler.joblib'))
        combined_features = joblib.load(os.path.join(MODEL_DIR, 'combined_features.joblib'))
        df = pd.read_csv(os.path.join(MODEL_DIR, 'exercises_enriched.csv'))
        model_loaded = True
        print("[AI Service] Modèle chargé avec succès depuis model/")
    except FileNotFoundError as e:
        print(f"[AI Service] ATTENTION: Modèle non trouvé ({e}). "
              "Exécutez le notebook d'abord. Utilisation du mode fallback.")
        model_loaded = False
        # Charger au moins le dataset brut pour le fallback
        try:
            df = pd.read_csv(os.path.join(DATA_DIR, 'exercises_dataset.csv'))
            print("[AI Service] Dataset brut chargé pour le mode fallback.")
        except Exception:
            df = None


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
# API Endpoints
# ============================================================

@app.route("/api/ai/health", methods=["GET"])
def health():
    """Health check — indique si le modèle ML est chargé."""
    return jsonify({
        "status": "ok",
        "model_loaded": model_loaded,
        "mode": "ml_model" if model_loaded else "fallback",
        "dataset_size": len(df) if df is not None else 0
    }), 200


@app.route("/api/ai/recommend", methods=["POST"])
def recommend():
    """
    Endpoint principal de recommandation.
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
    if model_loaded:
        recommendations = recommend_with_model(context, top_n=6)
        mode = "ml_model"
    else:
        recommendations = recommend_fallback(context, top_n=6)
        mode = "fallback"

    return jsonify({
        "status": "ok",
        "mode": mode,
        "message": "Recommandations générées avec succès"
                   + (" (modèle ML)" if model_loaded else " (mode fallback — exécutez le notebook)"),
        "nbRecommandations": len(recommendations),
        "recommandations": recommendations
    }), 200


@app.route("/api/ai/model-info", methods=["GET"])
def model_info():
    """Informations sur le modèle chargé."""
    info = {
        "model_loaded": model_loaded,
        "algorithm": "KNN (K=6) + TF-IDF + Cosine Similarity" if model_loaded else "Scoring heuristique (fallback)",
        "dataset_size": len(df) if df is not None else 0,
        "features": "TF-IDF (texte) + intensite_score + dureeMinutes + calories + type + difficulte" if model_loaded else "N/A",
        "metric": "cosine" if model_loaded else "N/A"
    }
    return jsonify(info), 200


# ============================================================
# Démarrage
# ============================================================
if __name__ == "__main__":
    load_model()
    print("\n[AI Service] Démarrage sur http://localhost:5000")
    print(f"[AI Service] Mode : {'ML Model' if model_loaded else 'Fallback (exécutez le notebook)'}")
    app.run(host="0.0.0.0", port=5000, debug=False)
