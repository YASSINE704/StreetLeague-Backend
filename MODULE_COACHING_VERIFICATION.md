# Vérification du module Gestion de Coaching

## Points validés / complétés

- Entités et associations JPA revues pour le module coaching.
- CRUD back-end complété pour toutes les ressources du module coaching:
  - `ProgrammeEntrainement`
  - `SeanceEntrainement`
  - `Exercice`
  - `SeanceExercice`
  - `SuiviSeance`
  - `AffectationProgramme`
- DTO conservés et validation renforcée.
- Spring Security v1 ajoutée en **HTTP Basic** avec rôles `ADMIN`, `COACH`, `SPORTIF`.
- Comptes de démonstration ajoutés au démarrage si la table `users` est vide.
- Dépendances existantes conservées sans changement de version.
- Aucun autre module métier n'a été modifié volontairement.

## Comptes de test

- `admin@streetleague.tn` / `admin123`
- `coach@streetleague.tn` / `coach123`
- `sportif@streetleague.tn` / `sportif123`

## Notes

- Les suppressions en cascade existent déjà via les associations JPA (`Programme -> Séances/Affectations`, `Séance -> Exercices/Suivi`).
- Les endpoints coaching sont protégés selon le rôle.
