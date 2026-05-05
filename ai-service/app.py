from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Base de 17 exercices couvrant les types FORCE, CARDIO, MOBILITE, TECHNIQUE
EXERCICES_BASE = [
    {
        "nom": "Pompes explosives",
        "type": "FORCE",
        "description": "Pompes avec phase explosive pour développer la puissance du haut du corps",
        "difficulte": "INTERMEDIAIRE",
        "dureeMinutes": 10,
        "equipement": "Aucun",
        "objectif": "puissance force explosivité haut du corps",
        "niveauRecommande": "INTERMEDIAIRE",
        "consigneSecurite": "Garder le dos droit, ne pas cambrer"
    },
    {
        "nom": "Squats sautés",
        "type": "FORCE",
        "description": "Squats avec saut pour renforcer les membres inférieurs",
        "difficulte": "INTERMEDIAIRE",
        "dureeMinutes": 12,
        "equipement": "Aucun",
        "objectif": "puissance jambes explosivité détente",
        "niveauRecommande": "INTERMEDIAIRE",
        "consigneSecurite": "Atterrir en douceur, genoux alignés avec les pieds"
    },
    {
        "nom": "Gainage dynamique",
        "type": "FORCE",
        "description": "Exercice de gainage avec mouvements pour renforcer le tronc",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 8,
        "equipement": "Tapis",
        "objectif": "stabilité core renforcement tronc",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Ne pas creuser le dos, respirer régulièrement"
    },
    {
        "nom": "Tractions australiennes",
        "type": "FORCE",
        "description": "Tractions horizontales pour le dos et les biceps",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 10,
        "equipement": "Barre basse",
        "objectif": "dos biceps tirage force",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Garder le corps aligné, contrôler la descente"
    },
    {
        "nom": "Sprint intervalles 30/30",
        "type": "CARDIO",
        "description": "Alternance de sprints de 30s et récupération de 30s",
        "difficulte": "AVANCE",
        "dureeMinutes": 15,
        "equipement": "Aucun",
        "objectif": "vitesse endurance cardio intervalles",
        "niveauRecommande": "AVANCE",
        "consigneSecurite": "Échauffement obligatoire, arrêter en cas de douleur"
    },
    {
        "nom": "Course continue modérée",
        "type": "CARDIO",
        "description": "Course à allure modérée pour développer l'endurance de base",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 20,
        "equipement": "Aucun",
        "objectif": "endurance fond cardio aérobie",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Maintenir une allure conversationnelle"
    },
    {
        "nom": "Burpees",
        "type": "CARDIO",
        "description": "Exercice complet combinant squat, planche et saut",
        "difficulte": "INTERMEDIAIRE",
        "dureeMinutes": 10,
        "equipement": "Aucun",
        "objectif": "cardio complet explosivité endurance",
        "niveauRecommande": "INTERMEDIAIRE",
        "consigneSecurite": "Adapter le rythme à son niveau, ne pas négliger la technique"
    },
    {
        "nom": "Corde à sauter",
        "type": "CARDIO",
        "description": "Sauts à la corde pour améliorer la coordination et le cardio",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 12,
        "equipement": "Corde à sauter",
        "objectif": "coordination cardio agilité rythme",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Surface souple, chaussures adaptées"
    },
    {
        "nom": "Étirements dynamiques",
        "type": "MOBILITE",
        "description": "Série d'étirements en mouvement pour préparer le corps",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 10,
        "equipement": "Aucun",
        "objectif": "souplesse échauffement mobilité articulaire",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Mouvements fluides, ne pas forcer"
    },
    {
        "nom": "Yoga sportif",
        "type": "MOBILITE",
        "description": "Postures de yoga adaptées aux sportifs pour la récupération",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 15,
        "equipement": "Tapis",
        "objectif": "récupération souplesse relaxation mobilité",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Respecter ses limites, ne pas forcer les postures"
    },
    {
        "nom": "Mobilité des hanches",
        "type": "MOBILITE",
        "description": "Exercices ciblés pour améliorer la mobilité des hanches",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 10,
        "equipement": "Aucun",
        "objectif": "hanches mobilité prévention blessures",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Mouvements contrôlés, pas de rebonds"
    },
    {
        "nom": "Foam rolling récupération",
        "type": "MOBILITE",
        "description": "Auto-massage avec rouleau pour la récupération musculaire",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 12,
        "equipement": "Foam roller",
        "objectif": "récupération massage détente musculaire",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Éviter les articulations, rouler lentement"
    },
    {
        "nom": "Dribble slalom",
        "type": "TECHNIQUE",
        "description": "Parcours de dribble entre plots pour améliorer le contrôle de balle",
        "difficulte": "INTERMEDIAIRE",
        "dureeMinutes": 15,
        "equipement": "Ballon, plots",
        "objectif": "dribble technique contrôle balle agilité",
        "niveauRecommande": "INTERMEDIAIRE",
        "consigneSecurite": "Surface plane, chaussures adaptées"
    },
    {
        "nom": "Passes courtes en mouvement",
        "type": "TECHNIQUE",
        "description": "Exercice de passes courtes avec déplacements",
        "difficulte": "DEBUTANT",
        "dureeMinutes": 12,
        "equipement": "Ballon",
        "objectif": "passes précision technique collective",
        "niveauRecommande": "DEBUTANT",
        "consigneSecurite": "Communication entre partenaires"
    },
    {
        "nom": "Tirs cadrés",
        "type": "TECHNIQUE",
        "description": "Exercice de tirs au but avec contraintes de placement",
        "difficulte": "INTERMEDIAIRE",
        "dureeMinutes": 15,
        "equipement": "Ballon, but",
        "objectif": "tir précision finition technique",
        "niveauRecommande": "INTERMEDIAIRE",
        "consigneSecurite": "Échauffement des jambes avant les frappes"
    },
    {
        "nom": "Jeu réduit 3v3",
        "type": "TECHNIQUE",
        "description": "Match en effectif réduit pour travailler la prise de décision",
        "difficulte": "INTERMEDIAIRE",
        "dureeMinutes": 20,
        "equipement": "Ballon, chasubles",
        "objectif": "tactique décision collective jeu",
        "niveauRecommande": "INTERMEDIAIRE",
        "consigneSecurite": "Respect des règles, pas de tacles dangereux"
    },
    {
        "nom": "Conduite de balle en vitesse",
        "type": "TECHNIQUE",
        "description": "Course avec ballon pour améliorer le contrôle à haute vitesse",
        "difficulte": "AVANCE",
        "dureeMinutes": 12,
        "equipement": "Ballon",
        "objectif": "vitesse technique contrôle conduite",
        "niveauRecommande": "AVANCE",
        "consigneSecurite": "Terrain dégagé, attention aux obstacles"
    }
]


def calculate_score(exercice, context):
    """
    Algorithme de scoring pour classer les exercices par pertinence.
    - Type match: +30
    - Intensité match: +20
    - Objectif keywords match: +15
    - Pas d'équipement requis: +10
    """
    score = 0

    # Type match (+30)
    type_seance = context.get("typeSeance", "").upper()
    if exercice["type"].upper() == type_seance:
        score += 30

    # Intensité match (+20)
    intensite = context.get("intensite", "").upper()
    difficulte = exercice.get("difficulte", "").upper()
    intensite_mapping = {
        "FAIBLE": "DEBUTANT",
        "MODEREE": "INTERMEDIAIRE",
        "MODERE": "INTERMEDIAIRE",
        "MOYENNE": "INTERMEDIAIRE",
        "ELEVEE": "AVANCE",
        "HAUTE": "AVANCE",
        "INTENSE": "AVANCE"
    }
    mapped_intensite = intensite_mapping.get(intensite, intensite)
    if mapped_intensite == difficulte:
        score += 20

    # Objectif keywords match (+15)
    objectif_programme = context.get("objectifProgramme", "").lower()
    exercice_objectif = exercice.get("objectif", "").lower()
    if objectif_programme:
        keywords = objectif_programme.split()
        for keyword in keywords:
            if len(keyword) > 3 and keyword in exercice_objectif:
                score += 15
                break

    # Pas d'équipement requis (+10)
    equipement = exercice.get("equipement", "")
    if equipement.lower() in ["aucun", "none", ""]:
        score += 10

    return score


@app.route("/api/ai/health", methods=["GET"])
def health():
    """Health check endpoint."""
    return jsonify({"status": "ok"}), 200


@app.route("/api/ai/recommend", methods=["POST"])
def recommend():
    """
    Reçoit un contexte JSON et retourne 6 recommandations d'exercices
    classées par score de pertinence.
    """
    context = request.get_json()
    if not context:
        return jsonify({
            "status": "error",
            "message": "Contexte JSON requis",
            "nbRecommandations": 0,
            "recommandations": []
        }), 400

    # Calculer le score pour chaque exercice
    scored_exercices = []
    for exercice in EXERCICES_BASE:
        score = calculate_score(exercice, context)
        exercice_with_score = dict(exercice)
        exercice_with_score["scoreRelevance"] = score
        exercice_with_score["raison"] = build_raison(exercice, context, score)
        scored_exercices.append(exercice_with_score)

    # Trier par score décroissant et prendre les 6 meilleurs
    scored_exercices.sort(key=lambda x: x["scoreRelevance"], reverse=True)
    top_recommendations = scored_exercices[:6]

    return jsonify({
        "status": "ok",
        "message": "Recommandations générées avec succès",
        "nbRecommandations": len(top_recommendations),
        "recommandations": top_recommendations
    }), 200


def build_raison(exercice, context, score):
    """Construit une explication de la recommandation."""
    raisons = []
    type_seance = context.get("typeSeance", "").upper()

    if exercice["type"].upper() == type_seance:
        raisons.append(f"correspond au type de séance ({type_seance})")

    intensite = context.get("intensite", "").upper()
    intensite_mapping = {
        "FAIBLE": "DEBUTANT",
        "MODEREE": "INTERMEDIAIRE",
        "MODERE": "INTERMEDIAIRE",
        "MOYENNE": "INTERMEDIAIRE",
        "ELEVEE": "AVANCE",
        "HAUTE": "AVANCE",
        "INTENSE": "AVANCE"
    }
    mapped = intensite_mapping.get(intensite, intensite)
    if mapped == exercice.get("difficulte", "").upper():
        raisons.append("intensité adaptée")

    objectif = context.get("objectifProgramme", "").lower()
    if objectif:
        keywords = objectif.split()
        for kw in keywords:
            if len(kw) > 3 and kw in exercice.get("objectif", "").lower():
                raisons.append(f"correspond à l'objectif ({kw})")
                break

    equipement = exercice.get("equipement", "")
    if equipement.lower() in ["aucun", "none", ""]:
        raisons.append("ne nécessite pas d'équipement")

    if not raisons:
        raisons.append("exercice complémentaire recommandé")

    return "Recommandé car : " + ", ".join(raisons)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
