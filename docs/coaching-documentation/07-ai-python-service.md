# 07 - Service IA Python (Flask)

## 🎯 Qu'est-ce que Flask ?

**Flask** = un micro-framework Python pour créer des API web.

### En mots simples :
Flask est comme un **petit serveur web** très simple. Il écoute les requêtes HTTP et renvoie des réponses JSON. C'est le "cerveau IA" de notre application.

### Pourquoi Python pour l'IA ?
- Python est le langage n°1 pour l'IA et le Machine Learning
- Syntaxe simple et lisible
- Bibliothèques puissantes (scikit-learn, TensorFlow, etc.)
- Dans notre cas : algorithme de scoring personnalisé

---

## 📍 Fichier : `ai-service/app.py`

## 🏗️ Structure du fichier

```
app.py
├── Imports et configuration Flask
├── EXERCICES_BASE (base de 17 exercices)
├── calculate_score() (algorithme de scoring)
├── build_raison() (explication de la recommandation)
├── Route /api/ai/health (vérification santé)
├── Route /api/ai/recommend (recommandations)
└── Lancement du serveur (port 5000)
```

---

## 📚 La base de 17 exercices

Le service contient une "base de données" en mémoire de 17 exercices répartis en 4 catégories :

| Type | Nombre | Exemples |
|------|--------|----------|
| **FORCE** | 4 | Pompes explosives, Squats sautés, Gainage dynamique, Tractions australiennes |
| **CARDIO** | 4 | Sprint intervalles, Course continue, Burpees, Corde à sauter |
| **MOBILITE** | 4 | Étirements dynamiques, Yoga sportif, Mobilité hanches, Foam rolling |
| **TECHNIQUE** | 5 | Dribble slalom, Passes courtes, Tirs cadrés, Jeu réduit 3v3, Conduite de balle |

### Structure d'un exercice :
```python
{
    "nom": "Pompes explosives",           # Nom de l'exercice
    "type": "FORCE",                      # Catégorie
    "description": "Pompes avec phase explosive...",  # Comment le faire
    "difficulte": "INTERMEDIAIRE",        # Niveau requis
    "dureeMinutes": 10,                   # Durée estimée
    "equipement": "Aucun",               # Matériel nécessaire
    "objectif": "puissance force explosivité haut du corps",  # Mots-clés
    "niveauRecommande": "INTERMEDIAIRE",  # Pour qui
    "consigneSecurite": "Garder le dos droit..."  # Sécurité
}
```

---

## 🧮 L'algorithme de scoring (calculate_score)

### Comment ça marche ?

L'algorithme donne un **score** à chaque exercice selon le contexte de la séance. Plus le score est élevé, plus l'exercice est pertinent.

```python
def calculate_score(exercice, context):
    score = 0

    # Critère 1 : Type match (+30 points)
    # Si le type de l'exercice correspond au type demandé
    if exercice["type"] == context["typeSeance"]:
        score += 30

    # Critère 2 : Intensité match (+20 points)
    # Si la difficulté correspond à l'intensité de la séance
    if mapped_intensite == difficulte:
        score += 20

    # Critère 3 : Objectif keywords match (+15 points)
    # Si un mot-clé de l'objectif se retrouve dans l'exercice
    if keyword in exercice_objectif:
        score += 15

    # Critère 4 : Pas d'équipement (+10 points)
    # Bonus si l'exercice ne nécessite pas de matériel
    if equipement == "Aucun":
        score += 10

    return score
```

### Tableau des points :

| Critère | Points | Condition |
|---------|--------|-----------|
| Type correspond | +30 | Ex: séance FORCE → exercice FORCE |
| Intensité correspond | +20 | Ex: séance MOYENNE → exercice INTERMEDIAIRE |
| Mot-clé objectif | +15 | Ex: objectif "force" → exercice contient "force" |
| Sans équipement | +10 | Ex: exercice ne nécessite rien |

### Score maximum possible : 75 points (30+20+15+10)

---

## 📝 Exemple concret pas à pas

### Contexte envoyé :
```json
{
  "objectifProgramme": "renforcement force",
  "typeSeance": "FORCE",
  "intensite": "MOYENNE",
  "nbParticipants": 5,
  "dureeSeanceMinutes": 60
}
```

### Calcul pour "Pompes explosives" :
1. **Type** : exercice.type = "FORCE", context.typeSeance = "FORCE" → **MATCH → +30**
2. **Intensité** : "MOYENNE" → mappé à "INTERMEDIAIRE", exercice.difficulte = "INTERMEDIAIRE" → **MATCH → +20**
3. **Objectif** : "renforcement force" contient "force", exercice.objectif contient "force" → **MATCH → +15**
4. **Équipement** : "Aucun" → **MATCH → +10**

**Score total : 75/75** ✅ (exercice très pertinent)

### Calcul pour "Yoga sportif" :
1. **Type** : exercice.type = "MOBILITE", context.typeSeance = "FORCE" → **PAS MATCH → +0**
2. **Intensité** : "MOYENNE" → "INTERMEDIAIRE", exercice.difficulte = "DEBUTANT" → **PAS MATCH → +0**
3. **Objectif** : "renforcement force" → aucun mot dans "récupération souplesse relaxation mobilité" → **PAS MATCH → +0**
4. **Équipement** : "Tapis" → **PAS MATCH → +0**

**Score total : 0/75** ❌ (exercice pas pertinent)

---

## 🗺️ Le mapping d'intensité

L'intensité de la séance (côté Spring Boot) ne correspond pas directement à la difficulté de l'exercice. Voici la table de conversion :

```python
intensite_mapping = {
    "FAIBLE": "DEBUTANT",
    "MODEREE": "INTERMEDIAIRE",
    "MODERE": "INTERMEDIAIRE",
    "MOYENNE": "INTERMEDIAIRE",
    "ELEVEE": "AVANCE",
    "HAUTE": "AVANCE",
    "INTENSE": "AVANCE"
}
```

| Intensité séance | → | Difficulté exercice |
|-----------------|---|-------------------|
| FAIBLE | → | DEBUTANT |
| MOYENNE/MODEREE | → | INTERMEDIAIRE |
| ELEVEE/HAUTE | → | AVANCE |

---

## 🛣️ Les routes (endpoints)

### Route 1 : Health Check

```python
@app.route("/api/ai/health", methods=["GET"])
def health():
    return jsonify({"status": "ok"}), 200
```

- **URL** : `GET http://localhost:5000/api/ai/health`
- **But** : Vérifier que le service est en vie
- **Réponse** : `{"status": "ok"}`

### Route 2 : Recommandations

```python
@app.route("/api/ai/recommend", methods=["POST"])
def recommend():
    context = request.get_json()  # Récupère le JSON envoyé
    if not context:
        return jsonify({...}), 400  # Erreur si pas de JSON

    # Calcule le score pour chaque exercice
    scored_exercices = []
    for exercice in EXERCICES_BASE:
        score = calculate_score(exercice, context)
        exercice_with_score = dict(exercice)
        exercice_with_score["scoreRelevance"] = score
        exercice_with_score["raison"] = build_raison(exercice, context, score)
        scored_exercices.append(exercice_with_score)

    # Trie par score décroissant et prend les 6 meilleurs
    scored_exercices.sort(key=lambda x: x["scoreRelevance"], reverse=True)
    top_recommendations = scored_exercices[:6]

    return jsonify({
        "status": "ok",
        "nbRecommandations": len(top_recommendations),
        "recommandations": top_recommendations
    }), 200
```

- **URL** : `POST http://localhost:5000/api/ai/recommend`
- **Body** : JSON avec le contexte de la séance
- **Réponse** : Les 6 exercices les plus pertinents, triés par score

---

## 💬 La fonction build_raison

Cette fonction génère une **explication en français** de pourquoi l'exercice est recommandé :

```python
def build_raison(exercice, context, score):
    raisons = []

    if type_match:
        raisons.append("correspond au type de séance (FORCE)")
    if intensite_match:
        raisons.append("intensité adaptée")
    if objectif_match:
        raisons.append("correspond à l'objectif (force)")
    if no_equipment:
        raisons.append("ne nécessite pas d'équipement")

    return "Recommandé car : " + ", ".join(raisons)
```

Exemple de sortie :
> "Recommandé car : correspond au type de séance (FORCE), intensité adaptée, correspond à l'objectif (force), ne nécessite pas d'équipement"

---

## 🚀 Comment lancer le service

### Prérequis :
- Python 3.8+ installé
- pip (gestionnaire de paquets Python)

### Étapes :
```bash
# 1. Aller dans le dossier
cd ai-service

# 2. Installer les dépendances
pip install -r requirements.txt

# 3. Lancer le serveur
python app.py
```

Le serveur démarre sur `http://localhost:5000`

### Dépendances (requirements.txt) :
```
flask
flask-cors
```

---

## 🧪 Comment tester avec curl

### Test 1 : Health check
```bash
curl http://localhost:5000/api/ai/health
```
Réponse attendue :
```json
{"status": "ok"}
```

### Test 2 : Recommandations
```bash
curl -X POST http://localhost:5000/api/ai/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "objectifProgramme": "renforcement musculaire",
    "typeSeance": "FORCE",
    "intensite": "MOYENNE",
    "nbParticipants": 5,
    "dureeSeanceMinutes": 60
  }'
```

Réponse attendue (extrait) :
```json
{
  "status": "ok",
  "nbRecommandations": 6,
  "recommandations": [
    {
      "nom": "Pompes explosives",
      "type": "FORCE",
      "scoreRelevance": 75,
      "raison": "Recommandé car : correspond au type de séance (FORCE), intensité adaptée, ..."
    },
    ...
  ]
}
```

### Test 3 : Erreur (pas de body)
```bash
curl -X POST http://localhost:5000/api/ai/recommend
```
Réponse : erreur 400

---

## 🎓 Questions du professeur

**Q : Pourquoi ne pas utiliser un vrai modèle de Machine Learning ?**
> R : Pour un projet académique, un algorithme de scoring basé sur des règles est suffisant et compréhensible. Un vrai ML nécessiterait des données d'entraînement massives. Notre approche est déterministe, explicable et maintenable.

**Q : Pourquoi 6 recommandations et pas plus ?**
> R : 6 est un bon compromis entre choix suffisant et surcharge d'information. Le coach peut voir assez d'options sans être submergé.

**Q : Comment l'algorithme gère-t-il le cas où aucun exercice ne correspond ?**
> R : Tous les exercices reçoivent un score (même 0). Les 6 meilleurs sont toujours retournés. Si aucun ne correspond parfaitement, les exercices "complémentaires" sont proposés avec la raison "exercice complémentaire recommandé".

**Q : Pourquoi Flask et pas Django ?**
> R : Flask est un micro-framework léger, parfait pour un petit service avec 2 routes. Django serait surdimensionné (il inclut ORM, admin, templates...). Flask = simplicité.

**Q : Qu'est-ce que CORS dans Flask ?**
> R : `CORS(app)` autorise les requêtes depuis n'importe quelle origine. Sans ça, Spring Boot ne pourrait pas appeler Flask (même problème que Angular → Spring Boot).

**Q : Que se passe-t-il si le service Python plante ?**
> R : Spring Boot a un mécanisme de fallback. Il catch l'exception et renvoie une réponse avec status "fallback" et un message "Service AI indisponible". L'application continue de fonctionner sans l'IA.

**Q : Pourquoi stocker les exercices en mémoire et pas en base de données ?**
> R : C'est un choix de simplicité pour le prototype. Les 17 exercices sont fixes et ne changent pas. En production, on utiliserait une BDD. Ici, ça évite une dépendance supplémentaire.
