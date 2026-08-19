export interface CoursRequest {
  typeDanseId: number;
  nom: string;
  jour: string;
  heureDebut: string;
  dureeMinutes: number;
  placesMax: number;
  placesDisponibles: number;
  statut: string;
}
