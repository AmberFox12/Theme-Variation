import { TypeDanse } from './type-danse.model';

export interface Cours {
  id: number;
  nom: string;
  jour: string;
  heureDebut: string;
  dureeMinutes: number;
  placesMax: number;
  placesDisponibles: number;
  statut: string;
  typeDanse: TypeDanse;
}
