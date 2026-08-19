import { Component, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { AgendaService } from '../../core/services/agenda.service';
import { CoursService } from '../../core/services/cours.service';
import { Evenement } from '../../core/models/evenement.model';
import { Cours } from '../../core/models/cours.model';

@Component({
  selector: 'app-agenda',
  imports: [DatePipe],
  templateUrl: './agenda.component.html',
  styleUrl: './agenda.component.scss'
})
export class AgendaComponent implements OnInit {
  prochainEvenement = signal<Evenement | null>(null);
  cours = signal<Cours[]>([]);

  jours = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'];
  heures = Array.from({ length: 30 }, (_, i) => {
    const heure = 8 + Math.floor(i / 2);
    const minute = i % 2 === 0 ? '00' : '30';
    return `${heure.toString().padStart(2, '0')}:${minute}`;
  });

  readonly PIXELS_PAR_HEURE = 60;
  readonly HEURE_DEBUT = 8;

  constructor(
    private agendaService: AgendaService,
    private coursService: CoursService
  ) {}

  ngOnInit(): void {
    this.agendaService.getProchainEvenement().subscribe({
      next: (data) => this.prochainEvenement.set(data),
      error: (err) => console.error('Erreur chargement événement :', err)
    });

    this.coursService.getCours().subscribe({
      next: (data) => this.cours.set(data),
      error: (err) => console.error('Erreur chargement planning :', err)
    });
  }

  getCoursParJour(jour: string): Cours[] {
    return this.cours().filter(c => c.jour === jour);
  }

  getTop(heureDebut: string): number {
    const [h, m] = heureDebut.split(':').map(Number);
    return (h - this.HEURE_DEBUT) * this.PIXELS_PAR_HEURE + (m / 60) * this.PIXELS_PAR_HEURE;
  }

  getHauteur(dureeMinutes: number): number {
    return (dureeMinutes / 60) * this.PIXELS_PAR_HEURE;
  }
}
