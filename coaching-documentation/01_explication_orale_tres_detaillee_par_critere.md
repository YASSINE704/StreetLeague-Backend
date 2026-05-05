# Explication orale très détaillée par critère d'évaluation

## Fonctionnalités avancées (/7)

J'ai dépassé le CRUD simple. Le module contient des règles métier et des fonctionnalités réelles : recherche, filtrage, tri, pagination, dashboard, export PDF, réservation, feedback, météo, notifications et IA.

### À l'aide de quoi ?

- Angular : `Array.filter()`, `Array.sort()`, `slice()`, `HttpClient`, composants, templates et CSS.
- Spring Boot : services métier, repositories JPA, DTOs validés, controllers REST.
- MySQL : persistance des programmes, séances, exercices, réservations, notifications.
- Scheduler Spring : vérification automatique des séances à venir.
- WeatherService : appel météo et recommandations pour séances extérieures.

### Phrase de soutenance

> Pour les fonctionnalités avancées, je n'ai pas simplement ajouté des boutons. J'ai ajouté des règles réalistes : une séance complète refuse les réservations, un sportif ne peut pas réserver deux séances au même horaire, une séance réalisée ne peut plus être modifiée, et une séance extérieure peut déclencher une alerte météo ou une recommandation de vêtements.

## Modèle IA (/4)

L'IA est un microservice Python Flask. Angular appelle Spring Boot, Spring Boot appelle Python, puis les résultats reviennent dans Angular. L'IA reçoit un contexte de séance : objectif, type, intensité, durée, niveau, participants, lieu et plein air. Elle retourne des propositions avec nom, description, type, difficulté, équipement, raison, sécurité et score.

### Phrase de soutenance

> L'IA ne modifie jamais automatiquement la séance. Elle propose des exercices. Le coach lit les cartes et clique sur “Ajouter à la séance” seulement pour les exercices qu'il valide. Cela garde le contrôle humain et respecte le rôle du coach.

## Ergonomie (/2)

L'interface suit le workflow naturel : programme → séance → exercices → suivi. Les boutons IA et ajout manuel sont placés dans le détail de la séance, parce que les exercices dépendent d'une séance précise. Les messages techniques comme “Python doit être lancé” ne sont pas affichés à l'utilisateur final.

## Scénario (/2)

1. Connexion coach.
2. Création d'un programme.
3. Création d'une séance.
4. Ajout manuel d'un exercice.
5. Génération IA.
6. Validation d'une proposition IA.
7. Connexion sportif.
8. Consultation/réservation.
9. Feedback après séance réalisée.
10. Dashboard et statistiques.

## Q&R (/5)

La réponse clé à plusieurs questions est :

> La logique métier est dans les services backend. Le frontend améliore l'expérience utilisateur, mais le backend protège réellement les règles.
