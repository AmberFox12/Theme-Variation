import {Component, OnInit, signal } from '@angular/core';
import { CoursService } from '../../core/services/cours.service';
import { Cours } from '../../core/models/cours.model';

@Component({
    selector: 'app-planning',
    imports: [],
    templateUrl: './planning.component.html',
    styleUrl: './planning.component.scss'
})
export class PlanningComponent implements OnInit {
    cours = signal<Cours[]>([]);

    jours =['Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi','Dimanche'];
    heures = Array.from({ length: 30 }, (_, i) => {
        const heure = 8 + Math.floor(i / 2);
        const minute = i % 2 === 0 ? '00' : '30';
        return `${heure.toString().padStart(2, '0')}:${minute}`;
    });

    readonly PIXELS_PAR_HEURE = 60;
    readonly HEURE_DEBUT = 8;

    constructor(private coursService : CoursService){}

    ngOnInit(): void {
        this.coursService.getCours().subscribe({
            next : (data) => this.cours.set(data),
            error : (err) => console.error('Erreur chargement planning', err)
        });
    }

    getCoursParJour(jour: string): Cours[] {
        return this.cours().filter(c => c.jour === jour);
    }
    getTop(heureDebut: string): number {
        const [h, m] = heureDebut.split(':').map(Number);
        return (h - this.HEURE_DEBUT) * this.PIXELS_PAR_HEURE + (m/60) * this.PIXELS_PAR_HEURE;
    }
    getHauteur(dureeMinutes: number): number {
        return (dureeMinutes / 60) * this.PIXELS_PAR_HEURE;
    }
}   