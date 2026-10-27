import { Component, OnInit, signal, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ParametreService } from '../../core/services/parametre.service';
import { Parametre } from '../../core/models/parametre.model';

@Component({
  selector: 'app-mentions-legales',
  imports: [RouterLink],
  templateUrl: './mentions-legales.component.html',
  styleUrl: './mentions-legales.component.scss'
})
export class MentionsLegalesComponent implements OnInit {
  private parametreService = inject(ParametreService);
  parametres = signal<Parametre | null>(null);

  ngOnInit(): void {
    this.parametreService.get().subscribe(p => this.parametres.set(p));
  }
}
