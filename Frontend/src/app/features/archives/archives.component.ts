import { Component, OnInit, signal } from '@angular/core';
import { ArchivesService, BACKEND_BASE } from '../../core/services/archives.service';
import { Historique } from '../../core/models/historique.model';
import { Spectacle } from '../../core/models/spectacle.model';

@Component({
  selector: 'app-archives',
  imports: [],
  templateUrl: './archives.component.html',
  styleUrl: './archives.component.scss'
})
export class ArchivesComponent implements OnInit {
  readonly backendBase = BACKEND_BASE;
  historique = signal<Historique[]>([]);
  spectacles = signal<Spectacle[]>([]);

  constructor(private archivesService: ArchivesService) {}

  ngOnInit(): void {
    this.archivesService.getHistorique().subscribe(data => this.historique.set(data));
    this.archivesService.getSpectacles().subscribe(data => this.spectacles.set(data));
  }

  spectaclesAVenir(): Spectacle[] {
    return this.spectacles().filter(s => s.statut === 'A_VENIR');
  }

  spectaclesPasses(): Spectacle[] {
    return this.spectacles().filter(s => s.statut === 'PASSE');
  }
}
