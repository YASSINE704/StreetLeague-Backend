export enum TypeEndroit {
  STADE = 'STADE',
  SALLE_SPORT = 'SALLE_SPORT',
  TERRAIN = 'TERRAIN'
}

export enum StatutEndroit {
  DISPONIBLE = 'DISPONIBLE',
  INDISPONIBLE = 'INDISPONIBLE',
  MAINTENANCE = 'MAINTENANCE'
}

export enum TypeSousEspace {
  TERRAIN = 'TERRAIN',
  SALLE = 'SALLE',
  COURT = 'COURT',
  ZONE = 'ZONE'
}

export enum StatutReservation {
  EN_ATTENTE = 'EN_ATTENTE',
  CONFIRMEE = 'CONFIRMEE',
  ANNULEE = 'ANNULEE'
}

export interface Endroit {
  id?: number;
  nom: string;
  type: TypeEndroit;
  adresse: string;
  ville: string;
  latitude: number;
  longitude: number;
  capacite: number;
  statut: StatutEndroit;
  description: string;
  imageUrl?: string;
  sousEspaces?: SousEspace[];
}

export interface SousEspace {
  id?: number;
  nom: string;
  type: TypeSousEspace;
  capacite: number;
  statut: StatutEndroit;
  endroitNom?: string;
  endroitId?: number;
  equipements?: Equipement[];
  reservations?: Reservation[];
}

export interface Equipement {
  id?: number;
  nom: string;
  quantite: number;
}

export interface Reservation {
  id?: number;
  dateDebut: string;
  dateFin: string;
  statut?: StatutReservation;
  dateCreation?: string;
  motifAnnulation?: string;
  sousEspaceNom?: string;
  sousEspaceId?: number;
  endroitNom?: string;
  endroitId?: number;
}
