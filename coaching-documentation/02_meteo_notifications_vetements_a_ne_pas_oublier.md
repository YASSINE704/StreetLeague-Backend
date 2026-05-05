# Météo, recommandations vêtements et notifications — partie importante

## Pourquoi cette partie est importante ?

Une séance en plein air dépend de la météo. Une application réaliste ne doit pas seulement enregistrer une séance : elle doit aussi aider le coach et les sportifs à anticiper les conditions.

## Ce qui est prévu techniquement

- `enPleinAir` dans `SeanceEntrainement` indique si la séance est extérieure.
- `SousEspace` permet de lier la séance à un lieu réel.
- `WeatherService` peut appeler OpenWeatherMap.
- `CoachingNotificationScheduler` vérifie automatiquement les séances à venir.
- `CoachingEmailService` prépare/envoie les emails.
- `NotificationCoaching` garde l'historique pour éviter les doublons.

## Recommandations possibles

- Pluie légère : vêtements imperméables, chaussures adaptées.
- Orage : déplacer en intérieur ou reporter.
- Froid : vêtements chauds, échauffement plus long.
- Chaleur : eau, pauses, vêtements légers.
- Vent fort : éviter certains exercices techniques ou matériel léger.

## Phrase à dire au prof

> Pour les séances en plein air, j'ai prévu une logique météo avec OpenWeatherMap. Le système peut recommander de déplacer la séance, de la reporter ou de conseiller des vêtements adaptés. Les notifications sont envoyées avant la séance avec un scheduler Spring, et elles sont enregistrées pour éviter les doublons.
