import { Component, OnInit, computed, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { AgendaService, EvenementRequest } from '../../../core/services/agenda.service';
import { Evenement } from '../../../core/models/evenement.model';

@Component({
  selector: 'app-evenements-admin',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './evenements-admin.component.html',
  styleUrl: './evenements-admin.component.scss'
})
export class EvenementsAdminComponent implements OnInit {

  evenements = signal<Evenement[]>([]);
  panneauOuvert = signal<boolean>(false);
  evenementEnEdition = signal<Evenement | null>(null);

  form = new FormGroup({
    titre: new FormControl('', Validators.required),
    description: new FormControl(''),
    dateHeure: new FormControl('', Validators.required),
    lieu: new FormControl('', Validators.required),
  });

  formStatus = toSignal(this.form.statusChanges, { initialValue: this.form.status });
  labelBouton = computed(() => this.evenementEnEdition() ? 'Enregistrer' : 'Créer');

  constructor(private agendaService: AgendaService) {}

  ngOnInit(): void {
    this.chargerEvenements();
  }

  chargerEvenements(): void {
    this.agendaService.getEvenements().subscribe(data => {
      this.evenements.set(data.sort((a, b) =>
        new Date(a.dateHeure).getTime() - new Date(b.dateHeure).getTime()
      ));
    });
  }

  ouvrirCreation(): void {
    this.evenementEnEdition.set(null);
    this.form.reset({ titre: '', description: '', dateHeure: '', lieu: '' });
    this.panneauOuvert.set(true);
  }

  ouvrirEdition(evenement: Evenement): void {
    this.evenementEnEdition.set(evenement);
    this.form.reset({
      titre: evenement.titre,
      description: evenement.description,
      dateHeure: evenement.dateHeure.slice(0, 16),
      lieu: evenement.lieu,
    });
    this.panneauOuvert.set(true);
  }

  fermerPanneau(): void {
    this.panneauOuvert.set(false);
  }

  sauvegarder(): void {
    if (this.form.invalid) return;
    const v = this.form.value;
    const request: EvenementRequest = {
      titre: v.titre!,
      description: v.description ?? '',
      dateHeure: v.dateHeure!,
      lieu: v.lieu!,
    };
    const enEdition = this.evenementEnEdition();
    const appel = enEdition
      ? this.agendaService.modifier(enEdition.id, request)
      : this.agendaService.creer(request);

    appel.subscribe(() => {
      this.chargerEvenements();
      this.fermerPanneau();
    });
  }

  supprimer(evenement: Evenement): void {
    if (!confirm(`Supprimer l'événement "${evenement.titre}" ?`)) return;
    this.agendaService.supprimer(evenement.id).subscribe({
      next: () => this.evenements.update(liste => liste.filter(e => e.id !== evenement.id)),
      error: err => console.error('Erreur suppression:', err)
    });
  }

  estPasse(dateHeure: string): boolean {
    return new Date(dateHeure) < new Date();
  }
}
