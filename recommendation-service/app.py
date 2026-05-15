"""
StreetLeague — Recommendation Web Service
Expose le modèle SVD entraîné via une API Flask.

Endpoints:
  GET  /health          — vérification que le service tourne
  POST /recommend       — recommandations pour un client
  POST /train           — ré-entraîner le modèle avec de nouvelles données
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import numpy as np
import joblib
import os
from surprise import SVD, Dataset, Reader

app = Flask(__name__)
CORS(app)

# ── Chargement du modèle et des données ─────────────────────────────────────
MODEL_PATH       = "svd_model.pkl"
INTERACTIONS_PATH = "interactions.csv"
TERRAINS_PATH    = "terrains.csv"

model        = None
df_interactions = None
df_terrains  = None


def load_artifacts():
    """Charge le modèle et les données depuis les fichiers."""
    global model, df_interactions, df_terrains

    if os.path.exists(MODEL_PATH):
        model = joblib.load(MODEL_PATH)
        print(f"[OK] Modèle chargé depuis {MODEL_PATH}")
    else:
        print(f"[WARN] Modèle introuvable ({MODEL_PATH}). Lance le notebook d'abord.")

    if os.path.exists(INTERACTIONS_PATH):
        df_interactions = pd.read_csv(INTERACTIONS_PATH)
        print(f"[OK] {len(df_interactions)} interactions chargées")
    else:
        df_interactions = pd.DataFrame(columns=["client_id", "terrain_id", "score"])

    if os.path.exists(TERRAINS_PATH):
        df_terrains = pd.read_csv(TERRAINS_PATH)
        print(f"[OK] {len(df_terrains)} terrains chargés")
    else:
        df_terrains = pd.DataFrame(columns=["id", "nom", "type", "disponible", "prix_base"])


load_artifacts()


# ── Endpoints ────────────────────────────────────────────────────────────────

@app.route("/health", methods=["GET"])
def health():
    """Vérifie que le service est opérationnel."""
    return jsonify({
        "status": "ok",
        "service": "StreetLeague Recommendation Service",
        "model_loaded": model is not None,
        "interactions_count": len(df_interactions) if df_interactions is not None else 0,
        "terrains_count": len(df_terrains) if df_terrains is not None else 0
    })


@app.route("/recommend", methods=["POST"])
def recommend():
    """
    Recommande des terrains pour un client.

    Body JSON attendu:
    {
        "client_id": 1,
        "top_n": 5,
        "terrains": [                          // optionnel: liste live depuis la DB
            {"id": 1, "nom": "...", "type": "...", "disponible": true, "prix_base": 20},
            ...
        ]
    }

    Retourne:
    {
        "client_id": 1,
        "recommendations": [
            {"terrain_id": 3, "nom": "...", "type": "...", "score_predit": 4.2, ...},
            ...
        ]
    }
    """
    global model, df_interactions, df_terrains

    if model is None:
        return jsonify({"error": "Modèle non chargé. Lance le notebook d'abord."}), 503

    data = request.get_json()
    if not data or "client_id" not in data:
        return jsonify({"error": "client_id requis"}), 400

    client_id = int(data["client_id"])
    top_n     = int(data.get("top_n", 5))

    # Utiliser les terrains envoyés dans la requête (données live de la DB)
    # ou fallback sur le fichier CSV
    if "terrains" in data and data["terrains"]:
        terrains_df = pd.DataFrame(data["terrains"])
        # Normaliser le champ disponible
        terrains_df["disponible"] = terrains_df["disponible"].astype(bool)
    else:
        terrains_df = df_terrains.copy()

    # Terrains déjà réservés par ce client
    if df_interactions is not None and not df_interactions.empty:
        deja_reserves = set(
            df_interactions[df_interactions["client_id"] == client_id]["terrain_id"].tolist()
        )
    else:
        deja_reserves = set()

    # Candidats : disponibles et pas encore réservés
    candidats = terrains_df[
        (terrains_df["disponible"] == True) &
        (~terrains_df["id"].isin(deja_reserves))
    ]

    if candidats.empty:
        return jsonify({
            "client_id": client_id,
            "recommendations": [],
            "message": "Aucun terrain disponible à recommander"
        })

    # Prédire le score pour chaque candidat
    recommendations = []
    for _, terrain in candidats.iterrows():
        pred = model.predict(client_id, int(terrain["id"]))
        recommendations.append({
            "terrain_id": int(terrain["id"]),
            "nom": terrain.get("nom", ""),
            "type": terrain.get("type", ""),
            "prix_base": float(terrain.get("prix_base", 0)),
            "disponible": bool(terrain.get("disponible", True)),
            "score_predit": round(float(pred.est), 3)
        })

    # Trier par score décroissant
    recommendations.sort(key=lambda x: x["score_predit"], reverse=True)

    return jsonify({
        "client_id": client_id,
        "nb_recommandations": len(recommendations[:top_n]),
        "deja_reserves": list(deja_reserves),
        "recommendations": recommendations[:top_n]
    })


@app.route("/train", methods=["POST"])
def train():
    """
    Ré-entraîne le modèle avec de nouvelles interactions.

    Body JSON:
    {
        "interactions": [
            {"client_id": 1, "terrain_id": 2, "score": 5},
            ...
        ]
    }
    """
    global model, df_interactions

    data = request.get_json()
    if not data or "interactions" not in data:
        return jsonify({"error": "interactions requises"}), 400

    new_df = pd.DataFrame(data["interactions"])

    # Fusionner avec les interactions existantes
    if df_interactions is not None and not df_interactions.empty:
        combined = pd.concat([df_interactions, new_df]).drop_duplicates(
            subset=["client_id", "terrain_id"], keep="last"
        )
    else:
        combined = new_df

    # Ré-entraîner
    reader  = Reader(rating_scale=(1, 5))
    dataset = Dataset.load_from_df(combined[["client_id", "terrain_id", "score"]], reader)
    trainset = dataset.build_full_trainset()

    new_model = SVD(n_factors=50, n_epochs=30, lr_all=0.005, reg_all=0.02, random_state=42)
    new_model.fit(trainset)

    # Sauvegarder
    joblib.dump(new_model, MODEL_PATH)
    combined.to_csv(INTERACTIONS_PATH, index=False)

    model           = new_model
    df_interactions = combined

    return jsonify({
        "status": "ok",
        "message": "Modèle ré-entraîné avec succès",
        "interactions_count": len(combined)
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5002, debug=False)
