# Code hyper commenté — `frontend/src/app/shared/models/programme-entrainement.model.ts`

## 1. Rôle du fichier

Fichier important du module.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **TypeScript / Angular**.
- Utilise Angular, TypeScript, RxJS/HttpClient, et le binding avec le template HTML.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `export interface ProgrammeEntrainement {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 2 | `  idProgramme?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 3 | `  titre: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 4 | `  description?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 5 | `  dateDebut: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 6 | `  dateFin: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `  statut?: StatutProgramme;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `  seances?: SeanceEntrainement[];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 9 | `  affectations?: AffectationProgramme[];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `export interface SeanceEntrainement {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 13 | `  idSeance?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `  titreSeance: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `  dateSeance: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `  heureDebut?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `  heureFin?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `  dureeMinutes?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `  maxParticipants?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `  placesRestantes?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `  intensite?: Intensite;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `  statut?: StatutSeance;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `  programmeId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `  programmeTitre?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `  sousEspaceId?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `  lieuNom?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `  endroitNom?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `  enPleinAir?: boolean;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `  exercices?: SeanceExercice[];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 30 | `  suiviSeance?: SuiviSeance;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `  reservations?: ReservationSeance[];` | Ligne liée à la réservation d’une séance par un sportif. |
| 32 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 33 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 34 | `export interface Exercice {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 35 | `  idExercice?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `  nom: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `  description?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 38 | `  type?: TypeExercice;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `  videoUrl?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 41 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 42 | `export interface SeanceExercice {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 43 | `  idSeanceExercice?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 44 | `  seanceId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 45 | `  seanceTitre?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 46 | `  exerciceId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 47 | `  exerciceNom?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `  series?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `  repetitions?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `  charge?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `  tempsSecondes?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `  ordre?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 54 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 55 | `export interface SuiviSeance {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 56 | `  idSuivi?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `  dateValidation?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `  ressenti?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 59 | `  fatigue?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `  commentaire?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `  seanceId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 63 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 64 | `export interface AffectationProgramme {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 65 | `  idAffectation?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 66 | `  type: TypeAffectationProgramme;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `  dateAffectation?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `  userId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `  userNom?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `  programmeId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 72 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 73 | `export enum StatutProgramme {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 74 | `  BROUILLON = 'BROUILLON',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `  ACTIF = 'ACTIF',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `  TERMINE = 'TERMINE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 77 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 78 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 79 | `export enum StatutSeance {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 80 | `  PREVUE = 'PREVUE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `  REALISEE = 'REALISEE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 82 | `  ANNULEE = 'ANNULEE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 83 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 84 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 85 | `export enum Intensite {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 86 | `  FAIBLE = 'FAIBLE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 87 | `  MOYENNE = 'MOYENNE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 88 | `  FORTE = 'FORTE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 89 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 90 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 91 | `export enum TypeExercice {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 92 | `  FORCE = 'FORCE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 93 | `  CARDIO = 'CARDIO',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 94 | `  MOBILITE = 'MOBILITE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 95 | `  TECHNIQUE = 'TECHNIQUE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 96 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 97 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 98 | `export enum TypeAffectationProgramme {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 99 | `  COACH = 'COACH',` | Ligne liée aux rôles et aux permissions utilisateur. |
| 100 | `  SPORTIF = 'SPORTIF'` | Ligne liée aux rôles et aux permissions utilisateur. |
| 101 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 102 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 103 | `/* ── Step 2 : Réservation de séance ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 104 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 105 | `export interface ReservationSeance {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 106 | `  idReservation?: number;` | Ligne liée à la réservation d’une séance par un sportif. |
| 107 | `  dateReservation?: string;` | Ligne liée à la réservation d’une séance par un sportif. |
| 108 | `  statut?: StatutReservationSeance;` | Ligne liée à la réservation d’une séance par un sportif. |
| 109 | `  modePaiement: ModePaiement;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 110 | `  motifAnnulation?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 111 | `  userId?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 112 | `  userNom?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 113 | `  seanceId: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 114 | `  seanceTitre?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 115 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 116 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 117 | `export enum StatutReservationSeance {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 118 | `  RESERVEE = 'RESERVEE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 119 | `  CONFIRMEE = 'CONFIRMEE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 120 | `  ANNULEE = 'ANNULEE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 121 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 122 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 123 | `export enum ModePaiement {` | Déclare une liste fermée de valeurs possibles pour éviter les textes invalides. |
| 124 | `  EN_LIGNE = 'EN_LIGNE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 125 | `  SUR_PLACE = 'SUR_PLACE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 126 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `programme-entrainement.model.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.