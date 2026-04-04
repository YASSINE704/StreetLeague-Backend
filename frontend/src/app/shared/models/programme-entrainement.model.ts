export interface ProgrammeEntrainement {
  idProgramme?: number;
  titre: string;
  description?: string;
  dateDebut: string;
  dateFin: string;
  statut?: StatutProgramme;
  seances?: SeanceEntrainement[];
  affectations?: AffectationProgramme[];
}

export interface SeanceEntrainement {
  idSeance?: number;
  titreSeance: string;
  dateSeance: string;
  dureeMinutes?: number;
  intensite?: Intensite;
  statut?: StatutSeance;
  programmeId: number;
  programmeTitre?: string;
  exercices?: SeanceExercice[];
  suiviSeance?: SuiviSeance;
}

export interface Exercice {
  idExercice?: number;
  nom: string;
  description?: string;
  type?: TypeExercice;
  videoUrl?: string;
}

export interface SeanceExercice {
  idSeanceExercice?: number;
  seanceId: number;
  seanceTitre?: string;
  exerciceId: number;
  exerciceNom?: string;
  series?: number;
  repetitions?: number;
  charge?: number;
  tempsSecondes?: number;
  ordre?: number;
}

export interface SuiviSeance {
  idSuivi?: number;
  dateValidation?: string;
  ressenti?: number;
  fatigue?: number;
  commentaire?: string;
  seanceId: number;
}

export interface AffectationProgramme {
  idAffectation?: number;
  type: TypeAffectationProgramme;
  dateAffectation?: string;
  userId: number;
  userNom?: string;
  programmeId: number;
}

export enum StatutProgramme {
  BROUILLON = 'BROUILLON',
  ACTIF = 'ACTIF',
  TERMINE = 'TERMINE'
}

export enum StatutSeance {
  PREVUE = 'PREVUE',
  REALISEE = 'REALISEE',
  ANNULEE = 'ANNULEE'
}

export enum Intensite {
  FAIBLE = 'FAIBLE',
  MOYENNE = 'MOYENNE',
  FORTE = 'FORTE'
}

export enum TypeExercice {
  FORCE = 'FORCE',
  CARDIO = 'CARDIO',
  MOBILITE = 'MOBILITE',
  TECHNIQUE = 'TECHNIQUE'
}

export enum TypeAffectationProgramme {
  COACH = 'COACH',
  SPORTIF = 'SPORTIF'
}
