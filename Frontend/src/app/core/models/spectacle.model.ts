export interface Spectacle {
  id: number;
  titre: string;
  annee: string;
  lieu: string;
  description: string;
  statut: string; // 'PASSE' | 'A_VENIR'
  imageUrl?: string;
}
