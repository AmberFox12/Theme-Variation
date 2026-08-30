import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AgendaService } from '../../core/services/agenda.service';
import { Evenement } from '../../core/models/evenement.model';

@Component({
  selector: 'app-home',
  imports: [RouterLink, DatePipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  prochainEvenement = signal<Evenement | null>(null);

  constructor(private agendaService: AgendaService) {}

  scrollVersCours(): void {
    document.querySelector('.cours')?.scrollIntoView({ behavior: 'smooth' });
  }

  ngOnInit(): void {
    this.agendaService.getProchainEvenement().subscribe({
      next: data => this.prochainEvenement.set(data),
      error: () => {}
    });
  }
}
