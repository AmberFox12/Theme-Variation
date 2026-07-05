import { Component, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { AgendaService } from '../../core/services/agenda.service';
import { Evenement } from '../../core/models/evenement.model';

@Component({
  selector: 'app-agenda',
  imports: [DatePipe],
  templateUrl: './agenda.component.html',
  styleUrl: './agenda.component.scss'
})
export class AgendaComponent implements OnInit {
  evenements = signal<Evenement[]>([]);

  constructor(private agendaService: AgendaService) {}

  ngOnInit(): void {
    this.agendaService.getEvenements().subscribe({
      next: (data) => this.evenements.set(data),
      error: (err) => console.error('Erreur chargement agenda :', err)
    });
  }
}
