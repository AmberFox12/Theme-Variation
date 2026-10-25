export interface Evenement {
    id: number;
    titre: string;
    description: string;
    dateHeure: string;
    dateFin?: string;
    lieu: string;
    imageUrl?: string;
}
