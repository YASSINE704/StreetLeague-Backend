# 11 - Scénario de Démonstration

## 🎯 Objectif de la démo

Montrer au professeur le **flux complet** du module coaching :
1. Création d'un programme
2. Création d'une séance
3. Ajout d'exercices manuellement
4. Utilisation de l'IA pour recommander des exercices
5. Validation d'un exercice IA
6. Suivi post-séance

---

## ⚙️ Prérequis (ce qu'il faut lancer AVANT la démo)

### Terminal 1 : Base de données + Backend Spring Boot
```bash
cd backend
./mvnw spring-boot:run
```
Attendre le message : `Started BackendApplication in X seconds`
→ Le backend tourne sur **http://localhost:8080**

### Terminal 2 : Service IA Python
```bash
cd ai-service
pip install -r requirements.txt
python app.py
```
Attendre le message : `Running on http://0.0.0.0:5000`
→ L'IA tourne sur **http://localhost:5000**

### Terminal 3 : Frontend Angular
```bash
cd frontend
npm install
ng serve
```
Attendre le message : `Compiled successfully`
→ Le frontend tourne sur **http://localhost:4200**

### Vérification rapide :
- Ouvrir http://localhost:4200 → page de login
- Ouvrir http://localhost:5000/api/ai/health → `{"status": "ok"}`

---

## 👤 Connexion

1. Ouvrir **http://localhost:4200**
2. Se connecter avec un compte COACH :
   - Email : `coach@streetleague.com`
   - Mot de passe : `password123` (ou celui configuré dans DemoDataLoader)
3. Naviguer vers le module **Coaching** dans le menu

### 💬 Ce qu'on peut dire :
> "Je me connecte en tant que coach. Le système vérifie mon rôle via JWT et me donne accès au module coaching."

---

## 📋 Étape 1 : Créer un programme

1. Cliquer sur **"Programmes"** dans le menu coaching
2. Cliquer sur **"+ Nouveau programme"**
3. Remplir le formulaire :
   - Titre : `Programme Explosivité Q1`
   - Description : `Programme de développement de la puissance et explosivité`
   - Date début : `2025-01-06`
   - Date fin : `2025-03-28`
   - Statut : `ACTIF`
4. Cliquer **"Créer"**

### 💬 Ce qu'on peut dire :
> "Le programme est créé avec le statut ACTIF. Les validations vérifient que le titre fait entre 3 et 100 caractères, que les dates sont renseignées, et que la date de fin est après la date de début."

### ⚠️ Si erreur :
- "Le titre est obligatoire" → le champ titre est vide
- "La date de fin doit être après la date de début" → inverser les dates

---

## 📅 Étape 2 : Créer une séance

1. Dans les détails du programme, cliquer **"+ Nouvelle séance"**
2. Remplir :
   - Titre : `Séance Force Explosive`
   - Date : `2025-01-15` (dans l'intervalle du programme)
   - Durée : `60` minutes
   - Intensité : `MOYENNE`
3. Cliquer **"Créer"**

### 💬 Ce qu'on peut dire :
> "La séance est créée avec le statut PREVUE par défaut. Le système vérifie que la date est dans l'intervalle du programme. La durée est validée entre 1 et 300 minutes."

### ⚠️ Si erreur :
- "La date doit être comprise entre..." → la date est hors du programme
- Vérifier que le programmeId est bien renseigné

---

## 🏋️ Étape 3 : Ajouter un exercice manuellement

1. Cliquer sur la séance pour voir ses détails
2. Cliquer **"+ Ajouter"**
3. Dans le formulaire :
   - Exercice : choisir dans la liste déroulante (ex: un exercice existant)
   - Séries : `4`
   - Répétitions : `10`
   - Charge : `0`
   - Ordre : `1`
4. Cliquer **"Ajouter à la séance"**

### 💬 Ce qu'on peut dire :
> "L'ajout manuel permet de choisir un exercice du catalogue et de configurer les paramètres. Le système vérifie qu'il y a au moins des répétitions OU un temps d'exécution."

---

## 🤖 Étape 4 : Utiliser l'IA (POINT FORT DE LA DÉMO)

1. Cliquer **"🤖 IA"** pour ouvrir le panneau
2. Montrer les champs pré-remplis :
   - Objectif : déjà rempli avec le titre du programme
   - Type : FORCE (modifiable)
   - Niveau : Tous niveaux
3. Modifier l'objectif : `explosivité puissance force`
4. Garder le type : `FORCE`
5. Cliquer **"✨ Proposer avec IA"**
6. Attendre 1-2 secondes (spinner visible)
7. **6 cartes de recommandation apparaissent !**

### 💬 Ce qu'on peut dire :
> "L'IA analyse le contexte de la séance : l'objectif, le type, l'intensité, la durée. Elle utilise un algorithme de scoring qui attribue des points selon 4 critères : correspondance de type (+30), intensité adaptée (+20), mots-clés de l'objectif (+15), et absence d'équipement (+10). Les 6 exercices les plus pertinents sont proposés, triés par score décroissant."

### Points à montrer sur les cartes :
- Le **score** (ex: 75, 50, 30...)
- La **raison** ("Recommandé car : correspond au type, intensité adaptée...")
- Le **type** et la **difficulté** (badges colorés)
- La **consigne de sécurité**

---

## ✅ Étape 5 : Valider un exercice IA

1. Choisir l'exercice avec le meilleur score (ex: "Pompes explosives", score 75)
2. Cliquer **"Valider et ajouter à la séance"**
3. Observer :
   - Le bouton passe en "Ajout..."
   - La carte disparaît
   - Message vert : "Exercice IA ajouté à la séance"
   - L'exercice apparaît dans la liste en bas

### 💬 Ce qu'on peut dire :
> "Le coach garde le contrôle total. L'IA propose, mais c'est le coach qui décide. Quand il valide, le système vérifie d'abord si l'exercice existe dans le catalogue. S'il n'existe pas, il est créé automatiquement. Ensuite, il est lié à la séance avec des paramètres adaptés au type : 3 séries × 12 reps pour la force, ou 1 série chronométrée pour le cardio."

### Montrer la prévention des doublons :
- Si on essaie de valider un exercice déjà dans la séance → message "déjà programmé"

---

## 📊 Étape 6 : Suivi (optionnel)

1. Modifier le statut de la séance à **REALISEE** (via édition)
2. Revenir aux détails de la séance
3. Cliquer **"+ Ajouter le suivi"**
4. Remplir :
   - Ressenti : `8` /10
   - Fatigue : `6` /10
   - Commentaire : `Bonne séance, exercices IA pertinents`
5. Valider

### 💬 Ce qu'on peut dire :
> "Le suivi ne peut être créé que pour une séance REALISEE. C'est une règle métier. Le sportif note son ressenti et sa fatigue de 1 à 10. Un seul suivi par séance est autorisé."

---

## 🎯 Points clés à souligner pendant la démo

### Architecture :
> "3 services indépendants qui communiquent en REST/JSON : Angular pour l'interface, Spring Boot pour la logique métier et la sécurité, Flask pour l'intelligence artificielle."

### Sécurité :
> "Seuls les COACH et ADMIN ont accès au module coaching. La vérification se fait à deux niveaux : Spring Security (JWT + rôles) et CoachingRoleService (header X-User-Id)."

### IA :
> "L'algorithme de scoring est déterministe et explicable. Chaque recommandation est accompagnée d'une raison. Le coach garde le contrôle total."

### Résilience :
> "Si le service IA tombe en panne, l'application continue de fonctionner. Le fallback affiche un message et le coach peut utiliser l'ajout manuel."

### Règles métier :
> "Le système empêche les incohérences : pas de séance hors dates du programme, pas de modification d'une séance réalisée, pas de suivi sans réalisation."

---

## 🚨 Gestion des erreurs pendant la démo

### Si le backend ne démarre pas :
- Vérifier que le port 8080 n'est pas déjà utilisé
- Vérifier les logs dans le terminal

### Si l'IA ne répond pas :
- Vérifier que `python app.py` tourne
- Tester : `curl http://localhost:5000/api/ai/health`
- **Plan B** : montrer le fallback ("Service IA indisponible") et l'ajout manuel

### Si Angular ne compile pas :
- `npm install` pour installer les dépendances
- Vérifier les erreurs TypeScript dans le terminal

### Si la connexion échoue :
- Vérifier que le DemoDataLoader a créé les utilisateurs
- Vérifier les credentials dans la console H2 (http://localhost:8080/h2-console)

---

## ⏱️ Timing suggéré (15-20 minutes)

| Étape | Durée | Contenu |
|-------|-------|---------|
| Introduction | 2 min | Architecture, technologies |
| Connexion + Programme | 3 min | Login, création programme |
| Séance + Ajout manuel | 3 min | Création séance, ajout exercice |
| **IA (point fort)** | 5 min | Panneau IA, recommandations, validation |
| Suivi | 2 min | Feedback post-séance |
| Questions | 5 min | Réponses aux questions du prof |
