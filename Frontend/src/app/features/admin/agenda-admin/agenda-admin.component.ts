import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { CoursService } from '../../../core/services/cours.service';
import { Cours } from '../../../core/models/cours.model';
import { CoursRequest } from '../../../core/models/cours-request.model';
import { TypeDanse } from '../../../core/models/type-danse.model';

@Component({
  selector: 'app-agenda-admin',
  imports: [ReactiveFormsModule],
  templateUrl: './agenda-admin.component.html',
  styleUrl: './agenda-admin.component.scss'
})
export class AgendaAdminComponent implements OnInit {

  cours = signal<Cours[]>([]);
  typesDanse = signal<TypeDanse[]>([]);
  panneauOuvert = signal<boolean>(false);
  coursEnEdition = signal<Cours | null>(null);

  jours = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'];
  heures = Array.from({ length: 27 }, (_, i) => {
    const h = 9 + Math.floor(i / 2);
    const m = i % 2 === 0 ? '00' : '30';
    return `${h.toString().padStart(2, '0')}:${m}`;
  });

  readonly PIXELS_PAR_HEURE = 80;
  readonly HEURE_DEBUT = 9;

  form = new FormGroup({
    typeDanseId: new FormControl<number | null>(null, Validators.required),
    nom: new FormControl('', Validators.required),
    jour: new FormControl('', Validators.required),
    heureDebut: new FormControl('', Validators.required),
    dureeMinutes: new FormControl(60, [Validators.required, Validators.min(15)]),
    placesMax: new FormControl(15, [Validators.required, Validators.min(1)]),
    placesDisponibles: new FormControl(15, [Validators.required, Validators.min(0)]),
    statut: new FormControl('OUVERT', Validators.required),
  });

  formStatus = toSignal(this.form.statusChanges, { initialValue: this.form.status });

  constructor(private coursService: CoursService) {}

  ngOnInit(): void {
    this.chargerCours();
    this.coursService.getTypesDanse().subscribe(data => this.typesDanse.set(data));
  }

  chargerCours(): void {
    this.coursService.getCours().subscribe(data => this.cours.set(data));
  }

  ouvrirCreation(): void {
    this.coursEnEdition.set(null);
    this.form.reset({
      typeDanseId: null, nom: '', jour: 'Lundi', heureDebut: '09:00',
      dureeMinutes: 60, placesMax: 15, placesDisponibles: 15, statut: 'OUVERT'
    });
    this.panneauOuvert.set(true);
  }

  ouvrirEdition(cours: Cours, event: Event): void {
    event.stopPropagation();
    this.coursEnEdition.set(cours);
    this.form.reset({
      typeDanseId: cours.typeDanse.id,
      nom: cours.nom,
      jour: cours.jour,
      heureDebut: cours.heureDebut.slice(0, 5),
      dureeMinutes: cours.dureeMinutes,
      placesMax: cours.placesMax,
      placesDisponibles: cours.placesDisponibles,
      statut: cours.statut,
    });
    this.panneauOuvert.set(true);
  }

  fermerPanneau(): void {
    this.panneauOuvert.set(false);
  }

  sauvegarder(): void {
    if (this.form.invalid) return;
    const v = this.form.value;
    const request: CoursRequest = {
      typeDanseId: Number(v.typeDanseId),
      nom: v.nom!,
      jour: v.jour!,
      heureDebut: v.heureDebut!,
      dureeMinutes: Number(v.dureeMinutes),
      placesMax: Number(v.placesMax),
      placesDisponibles: Number(v.placesDisponibles),
      statut: v.statut!,
    };
    const enEdition = this.coursEnEdition();
    const appel = enEdition
      ? this.coursService.modifier(enEdition.id, request)
      : this.coursService.creer(request);

    appel.subscribe(() => {
      this.chargerCours();
      this.fermerPanneau();
    });
  }

  supprimer(cours: Cours, event: Event): void {
    event.stopPropagation();
    if (!confirm(`Supprimer le cours "${cours.nom}" ?`)) return;
    this.coursService.supprimer(cours.id).subscribe({
      next: () => this.cours.update(liste => liste.filter(c => c.id !== cours.id)),
      error: err => console.error('Erreur suppression:', err)
    });
  }

  getCoursParJour(jour: string): Cours[] {
    return this.cours().filter(c => c.jour === jour);
  }

  getTop(heureDebut: string): number {
    const [h, m] = heureDebut.split(':').map(Number);
    return (h - this.HEURE_DEBUT) * this.PIXELS_PAR_HEURE + (m / 60) * this.PIXELS_PAR_HEURE + 3;
  }

  getHauteur(dureeMinutes: number): number {
    return (dureeMinutes / 60) * this.PIXELS_PAR_HEURE - 6;
  }
}
