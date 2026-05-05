# Code hyper commenté — `ai-service/app.py`

## 1. Rôle du fichier

Service IA Python Flask : génère les recommandations d’exercices.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Python / Flask**.
- Utilise Flask pour exposer des endpoints REST côté IA.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `"""` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 2 | `StreetLeague Coaching — AI Exercise Recommendation Service` | Ligne liée à la recommandation IA d’exercices. |
| 3 | `Step 9 : Service Python Flask qui propose des exercices au coach.` | Initialise ou utilise Flask pour exposer une API Python. |
| 4 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 5 | `Usage :` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 6 | `  pip install -r requirements.txt` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `  python app.py` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `Le service écoute sur http://localhost:5000` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `Spring Boot appelle POST /api/ai/recommend` | Ligne liée à la recommandation IA d’exercices. |
| 11 | `"""` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `from flask import Flask, request, jsonify` | Initialise ou utilise Flask pour exposer une API Python. |
| 14 | `from flask_cors import CORS` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `import random` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `app = Flask(__name__)` | Initialise ou utilise Flask pour exposer une API Python. |
| 18 | `CORS(app)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `# ══════════════════════════════════════════════════════════` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 21 | `# Base de connaissances d'exercices` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 22 | `# ══════════════════════════════════════════════════════════` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `EXERCICES_DB = [` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `    # FORCE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `    {"nom": "Pompes", "type": "FORCE", "description": "Exercice de poussée au poids du corps", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `     "dureeMinutes": 10, "equipement": "Aucun", "objectif": "Renforcement pectoraux et triceps",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Garder le dos droit, ne pas cambrer"},` | Ligne liée à la recommandation IA d’exercices. |
| 29 | `    {"nom": "Squats", "type": "FORCE", "description": "Flexion des jambes", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 30 | `     "dureeMinutes": 12, "equipement": "Aucun ou barre", "objectif": "Renforcement quadriceps et fessiers",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Genoux alignés avec les pieds"},` | Ligne liée à la recommandation IA d’exercices. |
| 32 | `    {"nom": "Développé couché", "type": "FORCE", "description": "Poussée horizontale avec barre", "difficulte": "FORTE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `     "dureeMinutes": 15, "equipement": "Banc + barre + poids", "objectif": "Force pectoraux",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `     "niveauRecommande": "Intermédiaire", "consigneSecurite": "Toujours avec un partenaire"},` | Ligne liée à la recommandation IA d’exercices. |
| 35 | `    {"nom": "Tractions", "type": "FORCE", "description": "Traction verticale au poids du corps", "difficulte": "FORTE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `     "dureeMinutes": 10, "equipement": "Barre de traction", "objectif": "Renforcement dos et biceps",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `     "niveauRecommande": "Intermédiaire", "consigneSecurite": "Mouvement contrôlé, pas de balancement"},` | Ligne liée à la recommandation IA d’exercices. |
| 38 | `    {"nom": "Fentes", "type": "FORCE", "description": "Pas en avant avec flexion", "difficulte": "FAIBLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `     "dureeMinutes": 10, "equipement": "Aucun", "objectif": "Renforcement jambes unilatéral",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `     "niveauRecommande": "Débutant", "consigneSecurite": "Genou avant ne dépasse pas les orteils"},` | Ligne liée à la recommandation IA d’exercices. |
| 41 | `    {"nom": "Planche", "type": "FORCE", "description": "Gainage statique", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 42 | `     "dureeMinutes": 5, "equipement": "Aucun", "objectif": "Renforcement core/abdominaux",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Corps aligné, ne pas creuser le dos"},` | Ligne liée à la recommandation IA d’exercices. |
| 44 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 45 | `    # CARDIO` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 46 | `    {"nom": "Burpees", "type": "CARDIO", "description": "Exercice complet haute intensité", "difficulte": "FORTE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 47 | `     "dureeMinutes": 8, "equipement": "Aucun", "objectif": "Endurance et explosivité",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `     "niveauRecommande": "Intermédiaire", "consigneSecurite": "Échauffement obligatoire"},` | Ligne liée à la recommandation IA d’exercices. |
| 49 | `    {"nom": "Course sur place", "type": "CARDIO", "description": "Course stationnaire", "difficulte": "FAIBLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `     "dureeMinutes": 15, "equipement": "Aucun", "objectif": "Échauffement et endurance",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `     "niveauRecommande": "Débutant", "consigneSecurite": "Chaussures adaptées"},` | Ligne liée à la recommandation IA d’exercices. |
| 52 | `    {"nom": "Jumping Jacks", "type": "CARDIO", "description": "Sauts avec écart bras et jambes", "difficulte": "FAIBLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `     "dureeMinutes": 5, "equipement": "Aucun", "objectif": "Échauffement cardio",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `     "niveauRecommande": "Débutant", "consigneSecurite": "Surface stable"},` | Ligne liée à la recommandation IA d’exercices. |
| 55 | `    {"nom": "Mountain Climbers", "type": "CARDIO", "description": "Montées de genoux en position planche", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `     "dureeMinutes": 8, "equipement": "Aucun", "objectif": "Cardio et renforcement core",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Rythme régulier"},` | Ligne liée à la recommandation IA d’exercices. |
| 58 | `    {"nom": "Corde à sauter", "type": "CARDIO", "description": "Sauts avec corde", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 59 | `     "dureeMinutes": 10, "equipement": "Corde à sauter", "objectif": "Endurance et coordination",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Surface amortissante recommandée"},` | Ligne liée à la recommandation IA d’exercices. |
| 61 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 62 | `    # MOBILITE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 63 | `    {"nom": "Étirements dynamiques", "type": "MOBILITE", "description": "Mouvements d'amplitude articulaire", "difficulte": "FAIBLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 64 | `     "dureeMinutes": 10, "equipement": "Aucun", "objectif": "Mobilité et prévention blessures",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Ne pas forcer les amplitudes"},` | Ligne liée à la recommandation IA d’exercices. |
| 66 | `    {"nom": "Yoga guerrier", "type": "MOBILITE", "description": "Postures de yoga pour la force et souplesse", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `     "dureeMinutes": 15, "equipement": "Tapis", "objectif": "Souplesse et équilibre",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Respiration contrôlée"},` | Ligne liée à la recommandation IA d’exercices. |
| 69 | `    {"nom": "Foam Rolling", "type": "MOBILITE", "description": "Auto-massage avec rouleau", "difficulte": "FAIBLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `     "dureeMinutes": 10, "equipement": "Rouleau de massage", "objectif": "Récupération musculaire",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Éviter les articulations"},` | Ligne liée à la recommandation IA d’exercices. |
| 72 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 73 | `    # TECHNIQUE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 74 | `    {"nom": "Dribble slalom", "type": "TECHNIQUE", "description": "Dribble entre cônes", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `     "dureeMinutes": 12, "equipement": "Ballon + cônes", "objectif": "Technique de dribble",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Surface plane"},` | Ligne liée à la recommandation IA d’exercices. |
| 77 | `    {"nom": "Passes courtes", "type": "TECHNIQUE", "description": "Exercice de passes à deux", "difficulte": "FAIBLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 78 | `     "dureeMinutes": 10, "equipement": "Ballon", "objectif": "Précision des passes",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `     "niveauRecommande": "Débutant", "consigneSecurite": "Communication entre partenaires"},` | Ligne liée à la recommandation IA d’exercices. |
| 80 | `    {"nom": "Tirs au but", "type": "TECHNIQUE", "description": "Exercice de frappe", "difficulte": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `     "dureeMinutes": 15, "equipement": "Ballon + but", "objectif": "Précision et puissance de frappe",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 82 | `     "niveauRecommande": "Tous niveaux", "consigneSecurite": "Échauffement des jambes avant"},` | Ligne liée à la recommandation IA d’exercices. |
| 83 | `]` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 84 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 85 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 86 | `def score_exercise(ex, context):` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 87 | `    """Calcule un score de pertinence pour un exercice selon le contexte."""` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 88 | `    score = 50  # score de base` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 89 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 90 | `    intensite = context.get("intensite", "MOYENNE")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 91 | `    objectif = context.get("objectifProgramme", "").lower()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 92 | `    nb_participants = context.get("nbParticipants", 5)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 93 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 94 | `    # Correspondance type` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 95 | `    if context.get("typeSeance") and ex["type"] == context["typeSeance"]:` | Condition : exécute le bloc seulement si la règle est vraie. |
| 96 | `        score += 30` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 97 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 98 | `    # Correspondance intensité/difficulté` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 99 | `    mapping = {"FAIBLE": "FAIBLE", "MOYENNE": "MOYENNE", "FORTE": "FORTE"}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 100 | `    if ex["difficulte"] == mapping.get(intensite, "MOYENNE"):` | Condition : exécute le bloc seulement si la règle est vraie. |
| 101 | `        score += 20` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 102 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 103 | `    # Objectif du programme` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 104 | `    if objectif:` | Condition : exécute le bloc seulement si la règle est vraie. |
| 105 | `        if any(mot in ex["objectif"].lower() for mot in objectif.split()):` | Condition : exécute le bloc seulement si la règle est vraie. |
| 106 | `            score += 15` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 107 | `        if any(mot in ex["description"].lower() for mot in objectif.split()):` | Condition : exécute le bloc seulement si la règle est vraie. |
| 108 | `            score += 10` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 109 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 110 | `    # Pas d'équipement si beaucoup de participants` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 111 | `    if nb_participants > 3 and ex["equipement"] == "Aucun":` | Condition : exécute le bloc seulement si la règle est vraie. |
| 112 | `        score += 10` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 113 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 114 | `    # Variété aléatoire` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 115 | `    score += random.randint(0, 10)` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 116 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 117 | `    return score` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 118 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 119 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 120 | `@app.route("/api/ai/recommend", methods=["POST"])` | Déclare une route HTTP dans le service Flask Python. |
| 121 | `def recommend():` | Ligne liée à la recommandation IA d’exercices. |
| 122 | `    """` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 123 | `    Reçoit le contexte d'une séance et retourne des exercices recommandés.` | Ligne liée à la recommandation IA d’exercices. |
| 124 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 125 | `    Body JSON attendu :` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 126 | `    {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 127 | `        "objectifProgramme": "renforcement musculaire",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 128 | `        "typeSeance": "FORCE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 129 | `        "intensite": "MOYENNE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 130 | `        "nbParticipants": 4,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 131 | `        "niveauJoueurs": "Intermédiaire",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 132 | `        "dureeSeanceMinutes": 60,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 133 | `        "lieuType": "SALLE",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 134 | `        "enPleinAir": false` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 135 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 136 | `    """` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 137 | `    context = request.get_json() or {}` | Récupère le JSON envoyé dans la requête HTTP. |
| 138 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 139 | `    # Scorer chaque exercice` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 140 | `    scored = [(ex, score_exercise(ex, context)) for ex in EXERCICES_DB]` | Boucle : répète une logique sur plusieurs éléments. |
| 141 | `    scored.sort(key=lambda x: x[1], reverse=True)` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 142 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 143 | `    # Retourner les 6 meilleurs` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 144 | `    recommendations = []` | Ligne liée à la recommandation IA d’exercices. |
| 145 | `    for ex, score in scored[:6]:` | Boucle : répète une logique sur plusieurs éléments. |
| 146 | `        recommendations.append({` | Ligne liée à la recommandation IA d’exercices. |
| 147 | `            "nom": ex["nom"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 148 | `            "type": ex["type"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 149 | `            "description": ex["description"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 150 | `            "difficulte": ex["difficulte"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 151 | `            "dureeMinutes": ex["dureeMinutes"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 152 | `            "equipement": ex["equipement"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 153 | `            "objectif": ex["objectif"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 154 | `            "niveauRecommande": ex["niveauRecommande"],` | Ligne liée à la recommandation IA d’exercices. |
| 155 | `            "consigneSecurite": ex["consigneSecurite"],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 156 | `            "scoreRelevance": score,` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 157 | `            "raison": f"Recommandé pour {context.get('intensite', 'MOYENNE')} intensité, "` | Ligne liée à la recommandation IA d’exercices. |
| 158 | `                      f"objectif: {ex['objectif']}"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 159 | `        })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 160 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 161 | `    return jsonify({` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 162 | `        "status": "ok",` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 163 | `        "nbRecommandations": len(recommendations),` | Ligne liée à la recommandation IA d’exercices. |
| 164 | `        "recommandations": recommendations` | Ligne liée à la recommandation IA d’exercices. |
| 165 | `    })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 166 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 167 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 168 | `@app.route("/api/ai/health", methods=["GET"])` | Déclare une route HTTP dans le service Flask Python. |
| 169 | `def health():` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 170 | `    """Health check endpoint."""` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 171 | `    return jsonify({"status": "ok", "service": "StreetLeague AI Coaching"})` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 172 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 173 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 174 | `if __name__ == "__main__":` | Condition : exécute le bloc seulement si la règle est vraie. |
| 175 | `    print("🤖 StreetLeague AI Service démarré sur http://localhost:5000")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 176 | `    app.run(host="0.0.0.0", port=5000, debug=True)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `app.py` est le microservice IA. Il reçoit un contexte de séance et retourne des recommandations d’exercices, mais il ne modifie jamais directement la base.