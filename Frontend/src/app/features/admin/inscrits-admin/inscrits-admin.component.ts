import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { DatePipe, SlicePipe } from '@angular/common';
import { InscriptionService } from '../../../core/services/inscription.service';
import { Eleve, Inscription } from '../../../core/models/inscription.model';
import { Cours } from '../../../core/models/cours.model';

@Component({
  selector: 'app-inscrits-admin',
  imports: [ReactiveFormsModule, DatePipe, SlicePipe],
  templateUrl: './inscrits-admin.component.html',
  styleUrl: './inscrits-admin.component.scss'
})
export class InscritsAdminComponent implements OnInit {

  inscriptions = signal<Inscription[]>([]);
  eleves = signal<Eleve[]>([]);
  cours = signal<Cours[]>([]);
  panneauOuvert = signal<boolean>(false);
  modeNouvelEleve = signal<boolean>(false);

  readonly STATUTS = ['EN_COURS', 'PAYE', 'IMPAYE', 'TERMINE'];

  formInscription = new FormGroup({
    eleveId: new FormControl<number | null>(null, Validators.required),
    coursId: new FormControl<number | null>(null, Validators.required),
    statut: new FormControl('EN_COURS', Validators.required),
  });

  formEleve = new FormGroup({
    nom: new FormControl('', Validators.required),
    prenom: new FormControl('', Validators.required),
    email: new FormControl(''),
    telephone: new FormControl(''),
    dateNaissance: new FormControl(''),
  });

  formStatus = toSignal(this.formInscription.statusChanges, { initialValue: this.formInscription.status });
  formEleveStatus = toSignal(this.formEleve.statusChanges, { initialValue: this.formEleve.status });

  constructor(private inscriptionService: InscriptionService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.inscriptionService.getAll().subscribe(data => this.inscriptions.set(data));
    this.inscriptionService.getEleves().subscribe(data => this.eleves.set(data));
    this.inscriptionService.getCours().subscribe(data => this.cours.set(data));
  }

  ouvrir(): void {
    this.formInscription.reset({ eleveId: null, coursId: null, statut: 'EN_COURS' });
    this.formEleve.reset({ nom: '', prenom: '', email: '', telephone: '', dateNaissance: '' });
    this.modeNouvelEleve.set(false);
    this.panneauOuvert.set(true);
  }

  fermer(): void {
    this.panneauOuvert.set(false);
  }

  creerEleve(): void {
    if (this.formEleve.invalid) return;
    const v = this.formEleve.value;
    this.inscriptionService.creerEleve({
      nom: v.nom!,
      prenom: v.prenom!,
      email: v.email ?? '',
      telephone: v.telephone ?? '',
      dateNaissance: v.dateNaissance ?? undefined,
    }).subscribe(eleve => {
      this.eleves.update(liste => [...liste, eleve]);
      this.formInscription.patchValue({ eleveId: eleve.id });
      this.modeNouvelEleve.set(false);
    });
  }

  sauvegarder(): void {
    if (this.formInscription.invalid) return;
    const v = this.formInscription.value;
    this.inscriptionService.creerInscription(
      Number(v.eleveId),
      Number(v.coursId),
      v.statut!
    ).subscribe(inscription => {
      this.inscriptions.update(liste => [...liste, inscription]);
      this.fermer();
    });
  }

  changerStatut(inscription: Inscription, statut: string): void {
    this.inscriptionService.updateStatut(inscription.id, statut).subscribe(updated => {
      this.inscriptions.update(liste =>
        liste.map(i => i.id === updated.id ? updated : i)
      );
    });
  }

  supprimer(inscription: Inscription): void {
    const nom = `${inscription.eleve.prenom} ${inscription.eleve.nom}`;
    if (!confirm(`Supprimer l'inscription de ${nom} au cours "${inscription.cours.nom}" ?`)) return;
    this.inscriptionService.supprimerInscription(inscription.id).subscribe(() => {
      this.inscriptions.update(liste => liste.filter(i => i.id !== inscription.id));
    });
  }

  labelStatut(statut: string): string {
    const labels: Record<string, string> = {
      EN_COURS: 'En cours',
      PAYE: 'Payé',
      IMPAYE: 'Impayé',
      TERMINE: 'Terminé',
    };
    return labels[statut] ?? statut;
  }
}
