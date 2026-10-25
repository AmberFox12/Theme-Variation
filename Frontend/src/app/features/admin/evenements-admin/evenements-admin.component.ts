import { Component, OnInit, computed, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { switchMap, of } from 'rxjs';
import { AgendaService } from '../../../core/services/agenda.service';
import { Evenement } from '../../../core/models/evenement.model';

@Component({
  selector: 'app-evenements-admin',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './evenements-admin.component.html',
  styleUrl: './evenements-admin.component.scss'
})
export class EvenementsAdminComponent implements OnInit {

  evenements = signal<Evenement[]>([]);
  panneauOuvert = signal(false);
  evenementEnEdition = signal<Evenement | null>(null);

  typeDate = signal<'date' | 'periode'>('date');

  imageFile = signal<File | null>(null);
  imagePreview = signal<string | null>(null);
  imageErreur = signal<string | null>(null);

  form = new FormGroup({
    titre:       new FormControl('', Validators.required),
    description: new FormControl(''),
    dateHeure:   new FormControl(''),
    dateDebut:   new FormControl(''),
    dateFin:     new FormControl(''),
    lieu:        new FormControl('', Validators.required),
  });

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
    this.typeDate.set('date');
    this.imageFile.set(null);
    this.imagePreview.set(null);
    this.imageErreur.set(null);
    this.form.reset({ titre: '', description: '', dateHeure: '', dateDebut: '', dateFin: '', lieu: '' });
    this.panneauOuvert.set(true);
  }

  ouvrirEdition(evenement: Evenement): void {
    this.evenementEnEdition.set(evenement);
    const estPeriode = !!evenement.dateFin;
    this.typeDate.set(estPeriode ? 'periode' : 'date');
    this.imageFile.set(null);
    this.imagePreview.set(evenement.imageUrl ?? null);
    this.imageErreur.set(null);
    this.form.reset({
      titre:       evenement.titre,
      description: evenement.description,
      dateHeure:   estPeriode ? '' : evenement.dateHeure.slice(0, 16),
      dateDebut:   estPeriode ? evenement.dateHeure.slice(0, 10) : '',
      dateFin:     estPeriode ? evenement.dateFin!.slice(0, 10) : '',
      lieu:        evenement.lieu,
    });
    this.panneauOuvert.set(true);
  }

  fermerPanneau(): void {
    this.panneauOuvert.set(false);
  }

  onImageChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    if (file && file.size > 5 * 1024 * 1024) {
      this.imageErreur.set('Le fichier est trop volumineux (max 5 Mo).');
      this.imageFile.set(null);
      input.value = '';
      return;
    }
    this.imageErreur.set(null);
    this.imageFile.set(file);
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => this.imagePreview.set(e.target?.result as string);
      reader.readAsDataURL(file);
    }
  }

  get formulaireValide(): boolean {
    if (this.form.get('titre')?.invalid || this.form.get('lieu')?.invalid) return false;
    if (this.typeDate() === 'date') {
      return !!this.form.get('dateHeure')?.value;
    }
    return !!this.form.get('dateDebut')?.value && !!this.form.get('dateFin')?.value;
  }

  sauvegarder(): void {
    if (!this.formulaireValide) return;
    const v = this.form.value;
    const file = this.imageFile();

    const request = {
      titre:       v.titre!,
      description: v.description ?? '',
      dateHeure:   this.typeDate() === 'date' ? v.dateHeure! : v.dateDebut!,
      dateFin:     this.typeDate() === 'periode' ? v.dateFin! : undefined,
      lieu:        v.lieu!,
    };

    const enEdition = this.evenementEnEdition();
    const appel = enEdition
      ? this.agendaService.modifier(enEdition.id, request)
      : this.agendaService.creer(request);

    appel.pipe(
      switchMap(evenement => file
        ? this.agendaService.uploaderImage(evenement.id, file)
        : of(evenement)
      )
    ).subscribe(() => {
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

  estPasse(e: Evenement): boolean {
    const ref = e.dateFin ?? e.dateHeure;
    return new Date(ref) < new Date();
  }

  estEnCours(e: Evenement): boolean {
    if (!e.dateFin) return false;
    const now = new Date();
    return new Date(e.dateHeure) <= now && new Date(e.dateFin) >= now;
  }

  labelStatut(e: Evenement): string {
    if (this.estEnCours(e)) return 'En cours';
    if (this.estPasse(e)) return 'Passé';
    return 'À venir';
  }
}
