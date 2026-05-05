# 01 - Architecture Overview (Vue d'ensemble)

## 🎯 De quoi parle ce projet ?

**PI_StreetLeague** est une application web de gestion d'une ligue de sport de rue (street league). Le **module Coaching** permet à un coach de :
- Créer des **programmes d'entraînement**
- Planifier des **séances** avec des exercices
- Utiliser une **Intelligence Artificielle** pour recommander des exercices
- Suivre la performance des sportifs (ressenti, fatigue)

---

## 🧱 Les 3 technologies principales

| Technologie | Rôle | Port |
|-------------|------|------|
| **Angular** (TypeScript) | Interface utilisateur (ce que l'utilisateur voit) | `http://localhost:4200` |
| **Spring Boot** (Java) | Serveur principal (logique métier, base de données) | `http://localhost:8080` |
| **Flask** (Python) | Service d'Intelligence Artificielle | `http://localhost:5000` |

### En mots simples :
- **Angular** = la vitrine du magasin (ce que le client voit)
- **Spring Boot** = l'arrière-boutique (où on gère les stocks, les commandes)
- **Flask/Python** = un consultant externe spécialisé (l'IA qui donne des conseils)

---

## 🔄 Comment les 3 services communiquent

```
┌─────────────────────┐
│   NAVIGATEUR WEB    │
│   (Angular)         │
│   Port 4200         │
└─────────┬───────────┘
          │ HTTP (REST API)
          │ JSON
          ▼
┌─────────────────────┐
│   SPRING BOOT       │
│   (Java Backend)    │
│   Port 8080         │
└─────────┬───────────┘
          │ HTTP (REST API)
          │ JSON
          ▼
┌─────────────────────┐
│   FLASK (Python)    │
│   Service IA        │
│   Port 5000         │
└─────────────────────┘
```

### Le flux complet :
1. L'utilisateur clique sur un bouton dans Angular
2. Angular envoie une requête HTTP à Spring Boot
3. Spring Boot traite la demande (vérifie les droits, valide les données)
4. Si besoin d'IA : Spring Boot appelle Flask (Python)
5. Flask calcule les recommandations et répond à Spring Boot
6. Spring Boot renvoie la réponse à Angular
7. Angular affiche le résultat à l'utilisateur

---

## 📡 Qu'est-ce qu'une REST API ?

**REST API** = un moyen standardisé pour que deux programmes communiquent via Internet.

### Analogie simple :
Imagine un restaurant :
- **Le client** (Angular) = passe une commande
- **Le serveur** (API) = transmet la commande à la cuisine
- **La cuisine** (Spring Boot) = prépare le plat
- **Le menu** = la documentation de l'API (quelles commandes sont possibles)

### Les 4 verbes HTTP :
| Verbe | Action | Exemple restaurant |
|-------|--------|-------------------|
| `GET` | Lire/Récupérer | "Montrez-moi le menu" |
| `POST` | Créer | "Je commande un plat" |
| `PUT` | Modifier | "Changez ma commande" |
| `DELETE` | Supprimer | "Annulez ma commande" |

### Format des données : JSON
```json
{
  "titre": "Programme Force",
  "description": "Programme de renforcement",
  "dateDebut": "2025-01-01",
  "dateFin": "2025-03-01"
}
```
JSON = un format texte simple pour échanger des données (comme un formulaire rempli).

---

## 🏗️ Qu'est-ce qu'un microservice ?

Un **microservice** = un petit programme indépendant qui fait UNE chose bien.

Dans notre projet :
- **Spring Boot** = microservice principal (gère tout le métier)
- **Flask** = microservice IA (ne fait que des recommandations)

### Avantages :
- Si l'IA tombe en panne, le reste de l'application continue de fonctionner
- On peut modifier l'IA sans toucher au reste
- Chaque service peut utiliser le langage le plus adapté (Python pour l'IA)

---

## 📁 Structure des dossiers

```
PI_StreetLeague/
├── frontend/                          ← Application Angular
│   └── src/app/
│       ├── core/services/             ← Services HTTP (appels API)
│       ├── features/coaching/         ← Module Coaching
│       │   ├── programmes/            ← Composants Programme
│       │   ├── seances/               ← Composants Séance
│       │   ├── exercices/             ← Composants Exercice
│       │   ├── suivis/                ← Composants Suivi
│       │   └── dashboard/             ← Tableau de bord
│       └── shared/models/             ← Modèles TypeScript
│
├── backend/                           ← Application Spring Boot
│   └── src/main/java/com/streetLeague/backend/
│       ├── config/                    ← Configuration (sécurité, CORS)
│       ├── controller/                ← Points d'entrée API (endpoints)
│       ├── dto/                       ← Objets de transfert de données
│       ├── entity/                    ← Entités JPA (tables de la BDD)
│       ├── enums/                     ← Énumérations (valeurs fixes)
│       ├── exception/                 ← Gestion des erreurs
│       ├── mapper/                    ← Conversion DTO ↔ Entity
│       ├── repository/                ← Accès base de données
│       └── service/                   ← Logique métier
│
└── ai-service/                        ← Service Python IA
    ├── app.py                         ← Code principal Flask
    └── requirements.txt               ← Dépendances Python
```

### Explication de chaque couche (Backend) :

```
Requête HTTP → Controller → Service → Repository → Base de données
                              ↕
                           Mapper
                              ↕
                            DTO
```

| Couche | Rôle | Analogie |
|--------|------|----------|
| **Controller** | Reçoit les requêtes HTTP | Le réceptionniste |
| **Service** | Contient la logique métier | Le manager |
| **Repository** | Communique avec la BDD | L'archiviste |
| **Entity** | Représente une table en BDD | Le formulaire papier |
| **DTO** | Données envoyées/reçues par l'API | L'enveloppe du courrier |
| **Mapper** | Convertit Entity ↔ DTO | Le traducteur |

---

## 🎓 Ce que le professeur pourrait demander

**Q : Pourquoi séparer en 3 services ?**
> R : Pour la séparation des responsabilités. Chaque service a un rôle précis. Si l'IA tombe, le reste fonctionne. On peut aussi utiliser le meilleur langage pour chaque tâche (Python pour l'IA).

**Q : Pourquoi utiliser REST et pas autre chose ?**
> R : REST est simple, standardisé, et fonctionne avec HTTP que tous les navigateurs comprennent. C'est le standard de l'industrie pour les API web.

**Q : Qu'est-ce que JSON ?**
> R : JavaScript Object Notation. C'est un format texte léger pour échanger des données structurées entre programmes. Il est lisible par les humains et facile à parser par les machines.

**Q : Quelle est la différence entre frontend et backend ?**
> R : Le frontend est ce que l'utilisateur voit et avec quoi il interagit (interface graphique). Le backend est la partie invisible qui gère la logique, les données et la sécurité.
