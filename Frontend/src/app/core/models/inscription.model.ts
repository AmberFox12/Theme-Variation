export interface Eleve {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  dateNaissance?: string;
}

export interface Inscription {
  id: number;
  eleve: Eleve;
  cours: {
    id: number;
    nom: string;
    jour: string;
    heureDebut: string;
  };
  statut: string;
  dateInscription: string;
}
