"""
StreetLeague Coaching — AI Exercise Recommendation Service
Step 9 : Service Python Flask qui propose des exercices au coach.

Usage :
  pip install -r requirements.txt
  python app.py

Le service écoute sur http://localhost:5000
Spring Boot appelle POST /api/ai/recommend
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import random

app = Flask(__name__)
CORS(app)

# ══════════════════════════════════════════════════════════
# Base de connaissances d'exercices
# ══════════════════════════════════════════════════════════

EXERCICES_DB = [
    # FORCE
    {"nom": "Pompes", "type": "FORCE", "description": "Exercice de poussée au poids du corps", "difficulte": "MOYENNE",
     "dureeMinutes": 10, "equipement": "Aucun", "objectif": "Renforcement pectoraux et triceps",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Garder le dos droit, ne pas cambrer"},
    {"nom": "Squats", "type": "FORCE", "description": "Flexion des jambes", "difficulte": "MOYENNE",
     "dureeMinutes": 12, "equipement": "Aucun ou barre", "objectif": "Renforcement quadriceps et fessiers",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Genoux alignés avec les pieds"},
    {"nom": "Développé couché", "type": "FORCE", "description": "Poussée horizontale avec barre", "difficulte": "FORTE",
     "dureeMinutes": 15, "equipement": "Banc + barre + poids", "objectif": "Force pectoraux",
     "niveauRecommande": "Intermédiaire", "consigneSecurite": "Toujours avec un partenaire"},
    {"nom": "Tractions", "type": "FORCE", "description": "Traction verticale au poids du corps", "difficulte": "FORTE",
     "dureeMinutes": 10, "equipement": "Barre de traction", "objectif": "Renforcement dos et biceps",
     "niveauRecommande": "Intermédiaire", "consigneSecurite": "Mouvement contrôlé, pas de balancement"},
    {"nom": "Fentes", "type": "FORCE", "description": "Pas en avant avec flexion", "difficulte": "FAIBLE",
     "dureeMinutes": 10, "equipement": "Aucun", "objectif": "Renforcement jambes unilatéral",
     "niveauRecommande": "Débutant", "consigneSecurite": "Genou avant ne dépasse pas les orteils"},
    {"nom": "Planche", "type": "FORCE", "description": "Gainage statique", "difficulte": "MOYENNE",
     "dureeMinutes": 5, "equipement": "Aucun", "objectif": "Renforcement core/abdominaux",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Corps aligné, ne pas creuser le dos"},

    # CARDIO
    {"nom": "Burpees", "type": "CARDIO", "description": "Exercice complet haute intensité", "difficulte": "FORTE",
     "dureeMinutes": 8, "equipement": "Aucun", "objectif": "Endurance et explosivité",
     "niveauRecommande": "Intermédiaire", "consigneSecurite": "Échauffement obligatoire"},
    {"nom": "Course sur place", "type": "CARDIO", "description": "Course stationnaire", "difficulte": "FAIBLE",
     "dureeMinutes": 15, "equipement": "Aucun", "objectif": "Échauffement et endurance",
     "niveauRecommande": "Débutant", "consigneSecurite": "Chaussures adaptées"},
    {"nom": "Jumping Jacks", "type": "CARDIO", "description": "Sauts avec écart bras et jambes", "difficulte": "FAIBLE",
     "dureeMinutes": 5, "equipement": "Aucun", "objectif": "Échauffement cardio",
     "niveauRecommande": "Débutant", "consigneSecurite": "Surface stable"},
    {"nom": "Mountain Climbers", "type": "CARDIO", "description": "Montées de genoux en position planche", "difficulte": "MOYENNE",
     "dureeMinutes": 8, "equipement": "Aucun", "objectif": "Cardio et renforcement core",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Rythme régulier"},
    {"nom": "Corde à sauter", "type": "CARDIO", "description": "Sauts avec corde", "difficulte": "MOYENNE",
     "dureeMinutes": 10, "equipement": "Corde à sauter", "objectif": "Endurance et coordination",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Surface amortissante recommandée"},

    # MOBILITE
    {"nom": "Étirements dynamiques", "type": "MOBILITE", "description": "Mouvements d'amplitude articulaire", "difficulte": "FAIBLE",
     "dureeMinutes": 10, "equipement": "Aucun", "objectif": "Mobilité et prévention blessures",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Ne pas forcer les amplitudes"},
    {"nom": "Yoga guerrier", "type": "MOBILITE", "description": "Postures de yoga pour la force et souplesse", "difficulte": "MOYENNE",
     "dureeMinutes": 15, "equipement": "Tapis", "objectif": "Souplesse et équilibre",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Respiration contrôlée"},
    {"nom": "Foam Rolling", "type": "MOBILITE", "description": "Auto-massage avec rouleau", "difficulte": "FAIBLE",
     "dureeMinutes": 10, "equipement": "Rouleau de massage", "objectif": "Récupération musculaire",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Éviter les articulations"},

    # TECHNIQUE
    {"nom": "Dribble slalom", "type": "TECHNIQUE", "description": "Dribble entre cônes", "difficulte": "MOYENNE",
     "dureeMinutes": 12, "equipement": "Ballon + cônes", "objectif": "Technique de dribble",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Surface plane"},
    {"nom": "Passes courtes", "type": "TECHNIQUE", "description": "Exercice de passes à deux", "difficulte": "FAIBLE",
     "dureeMinutes": 10, "equipement": "Ballon", "objectif": "Précision des passes",
     "niveauRecommande": "Débutant", "consigneSecurite": "Communication entre partenaires"},
    {"nom": "Tirs au but", "type": "TECHNIQUE", "description": "Exercice de frappe", "difficulte": "MOYENNE",
     "dureeMinutes": 15, "equipement": "Ballon + but", "objectif": "Précision et puissance de frappe",
     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Échauffement des jambes avant"},
]


def score_exercise(ex, context):
    """Calcule un score de pertinence pour un exercice selon le contexte."""
    score = 50  # score de base

    intensite = context.get("intensite", "MOYENNE")
    objectif = context.get("objectifProgramme", "").lower()
    nb_participants = context.get("nbParticipants", 5)

    # Correspondance type
    if context.get("typeSeance") and ex["type"] == context["typeSeance"]:
        score += 30

    # Correspondance intensité/difficulté
    mapping = {"FAIBLE": "FAIBLE", "MOYENNE": "MOYENNE", "FORTE": "FORTE"}
    if ex["difficulte"] == mapping.get(intensite, "MOYENNE"):
        score += 20

    # Objectif du programme
    if objectif:
        if any(mot in ex["objectif"].lower() for mot in objectif.split()):
            score += 15
        if any(mot in ex["description"].lower() for mot in objectif.split()):
            score += 10

    # Pas d'équipement si beaucoup de participants
    if nb_participants > 3 and ex["equipement"] == "Aucun":
        score += 10

    # Variété aléatoire
    score += random.randint(0, 10)

    return score


@app.route("/api/ai/recommend", methods=["POST"])
def recommend():
    """
    Reçoit le contexte d'une séance et retourne des exercices recommandés.

    Body JSON attendu :
    {
        "objectifProgramme": "renforcement musculaire",
        "typeSeance": "FORCE",
        "intensite": "MOYENNE",
        "nbParticipants": 4,
        "niveauJoueurs": "Intermédiaire",
        "dureeSeanceMinutes": 60,
        "lieuType": "SALLE",
        "enPleinAir": false
    }
    """
    context = request.get_json() or {}

    # Scorer chaque exercice
    scored = [(ex, score_exercise(ex, context)) for ex in EXERCICES_DB]
    scored.sort(key=lambda x: x[1], reverse=True)

    # Retourner les 6 meilleurs
    recommendations = []
    for ex, score in scored[:6]:
        recommendations.append({
            "nom": ex["nom"],
            "type": ex["type"],
            "description": ex["description"],
            "difficulte": ex["difficulte"],
            "dureeMinutes": ex["dureeMinutes"],
            "equipement": ex["equipement"],
            "objectif": ex["objectif"],
            "niveauRecommande": ex["niveauRecommande"],
            "consigneSecurite": ex["consigneSecurite"],
            "scoreRelevance": score,
            "raison": f"Recommandé pour {context.get('intensite', 'MOYENNE')} intensité, "
                      f"objectif: {ex['objectif']}"
        })

    return jsonify({
        "status": "ok",
        "nbRecommandations": len(recommendations),
        "recommandations": recommendations
    })


@app.route("/api/ai/health", methods=["GET"])
def health():
    """Health check endpoint."""
    return jsonify({"status": "ok", "service": "StreetLeague AI Coaching"})


if __name__ == "__main__":
    print("🤖 StreetLeague AI Service démarré sur http://localhost:5000")
    app.run(host="0.0.0.0", port=5000, debug=True)
