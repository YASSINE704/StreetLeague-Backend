"""
StreetLeague — Recommendation Web Service
Uses sklearn-based matrix factorization (SVD) instead of scikit-surprise
for Python 3.13+ compatibility.

Endpoints:
  GET  /health          — service health check
  POST /recommend       — recommendations for a client
  POST /train           — retrain model with new data
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import numpy as np
import joblib
import os
from sklearn.decomposition import TruncatedSVD
from sklearn.preprocessing import LabelEncoder

app = Flask(__name__)
CORS(app)

MODEL_PATH        = "svd_model.pkl"
INTERACTIONS_PATH = "interactions.csv"
TERRAINS_PATH     = "terrains.csv"

model            = None
df_interactions  = None
df_terrains      = None
user_encoder     = None
item_encoder     = None
user_item_matrix = None


def load_artifacts():
    global model, df_interactions, df_terrains, user_encoder, item_encoder, user_item_matrix

    if os.path.exists(MODEL_PATH):
        artifacts = joblib.load(MODEL_PATH)
        model            = artifacts.get("model")
        user_encoder     = artifacts.get("user_encoder")
        item_encoder     = artifacts.get("item_encoder")
        user_item_matrix = artifacts.get("user_item_matrix")
        print(f"[OK] Model loaded from {MODEL_PATH}")
    else:
        print(f"[WARN] Model not found ({MODEL_PATH}). Train first via POST /train.")

    if os.path.exists(INTERACTIONS_PATH):
        df_interactions = pd.read_csv(INTERACTIONS_PATH)
        print(f"[OK] {len(df_interactions)} interactions loaded")
    else:
        df_interactions = pd.DataFrame(columns=["client_id", "terrain_id", "score"])

    if os.path.exists(TERRAINS_PATH):
        df_terrains = pd.read_csv(TERRAINS_PATH)
        print(f"[OK] {len(df_terrains)} terrains loaded")
    else:
        df_terrains = pd.DataFrame(columns=["id", "nom", "type", "disponible", "prix_base"])


def train_model(interactions_df):
    """Train SVD model from interactions dataframe."""
    global model, user_encoder, item_encoder, user_item_matrix

    ue = LabelEncoder()
    ie = LabelEncoder()

    interactions_df = interactions_df.copy()
    interactions_df["user_idx"] = ue.fit_transform(interactions_df["client_id"])
    interactions_df["item_idx"] = ie.fit_transform(interactions_df["terrain_id"])

    n_users = interactions_df["user_idx"].max() + 1
    n_items = interactions_df["item_idx"].max() + 1

    matrix = np.zeros((n_users, n_items))
    for _, row in interactions_df.iterrows():
        matrix[int(row["user_idx"]), int(row["item_idx"])] = row["score"]

    n_components = min(20, n_users - 1, n_items - 1)
    if n_components < 1:
        n_components = 1

    svd = TruncatedSVD(n_components=n_components, random_state=42)
    svd.fit(matrix)

    user_item_matrix = matrix
    user_encoder     = ue
    item_encoder     = ie
    model            = svd

    return svd, ue, ie, matrix


def predict_score(client_id, terrain_id):
    """Predict score for a user-item pair."""
    if model is None or user_encoder is None or item_encoder is None:
        return 3.0  # default neutral score

    try:
        if client_id not in user_encoder.classes_:
            return 3.0
        if terrain_id not in item_encoder.classes_:
            return 3.0

        user_idx = user_encoder.transform([client_id])[0]
        item_idx = item_encoder.transform([terrain_id])[0]

        # Reconstruct matrix via SVD
        reconstructed = model.inverse_transform(model.transform(user_item_matrix))
        score = reconstructed[user_idx, item_idx]
        # Clamp to [1, 5]
        return float(np.clip(score, 1.0, 5.0))
    except Exception:
        return 3.0


load_artifacts()


@app.route("/health", methods=["GET"])
def health():
    return jsonify({
        "status": "ok",
        "service": "StreetLeague Recommendation Service (sklearn SVD)",
        "model_loaded": model is not None,
        "interactions_count": len(df_interactions) if df_interactions is not None else 0,
        "terrains_count": len(df_terrains) if df_terrains is not None else 0
    })


@app.route("/recommend", methods=["POST"])
def recommend():
    global model, df_interactions, df_terrains

    data = request.get_json()
    if not data or "client_id" not in data:
        return jsonify({"error": "client_id requis"}), 400

    client_id = int(data["client_id"])
    top_n     = int(data.get("top_n", 5))

    if "terrains" in data and data["terrains"]:
        terrains_df = pd.DataFrame(data["terrains"])
        terrains_df["disponible"] = terrains_df["disponible"].astype(bool)
    else:
        terrains_df = df_terrains.copy() if df_terrains is not None else pd.DataFrame()

    if terrains_df.empty:
        return jsonify({"client_id": client_id, "recommendations": [], "message": "No terrains available"})

    deja_reserves = set()
    if df_interactions is not None and not df_interactions.empty:
        deja_reserves = set(
            df_interactions[df_interactions["client_id"] == client_id]["terrain_id"].tolist()
        )

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

    recommendations = []
    for _, terrain in candidats.iterrows():
        score = predict_score(client_id, int(terrain["id"]))
        recommendations.append({
            "terrain_id": int(terrain["id"]),
            "nom": terrain.get("nom", ""),
            "type": terrain.get("type", ""),
            "prix_base": float(terrain.get("prix_base", 0)),
            "disponible": bool(terrain.get("disponible", True)),
            "score_predit": round(score, 3)
        })

    recommendations.sort(key=lambda x: x["score_predit"], reverse=True)

    return jsonify({
        "client_id": client_id,
        "nb_recommandations": len(recommendations[:top_n]),
        "deja_reserves": list(deja_reserves),
        "recommendations": recommendations[:top_n]
    })


@app.route("/train", methods=["POST"])
def train():
    global model, df_interactions

    data = request.get_json()
    if not data or "interactions" not in data:
        return jsonify({"error": "interactions requises"}), 400

    new_df = pd.DataFrame(data["interactions"])

    if df_interactions is not None and not df_interactions.empty:
        combined = pd.concat([df_interactions, new_df]).drop_duplicates(
            subset=["client_id", "terrain_id"], keep="last"
        )
    else:
        combined = new_df

    if len(combined) < 2:
        return jsonify({"error": "Need at least 2 interactions to train"}), 400

    svd, ue, ie, matrix = train_model(combined)

    joblib.dump({
        "model": svd,
        "user_encoder": ue,
        "item_encoder": ie,
        "user_item_matrix": matrix
    }, MODEL_PATH)

    combined.to_csv(INTERACTIONS_PATH, index=False)
    df_interactions = combined

    return jsonify({
        "status": "ok",
        "message": "Model retrained successfully",
        "interactions_count": len(combined)
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5002, debug=False)
