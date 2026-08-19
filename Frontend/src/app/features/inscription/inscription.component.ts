import { Component, OnInit, computed, signal } from '@angular/core';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionService } from '../../core/services/inscription.service';
import { Cours } from '../../core/models/cours.model';

@Component({
  selector: 'app-inscription',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription.component.html',
  styleUrl: './inscription.component.scss'
})
export class InscriptionComponent implements OnInit {

  cours = signal<Cours[]>([]);
  enCours = signal(false);
  succes = signal(false);
  erreurMessage = signal('');

  coursByType = computed(() => {
    const grouped = new Map<string, Cours[]>();
    for (const c of this.cours()) {
      const type = c.typeDanse?.nom ?? 'Autres';
      if (!grouped.has(type)) grouped.set(type, []);
      grouped.get(type)!.push(c);
    }
    return Array.from(grouped.entries()).map(([type, cours]) => ({ type, cours }));
  });

  selectedCours = signal<Set<number>[]>([]);

  form = new FormGroup({
    nom: new FormControl('', Validators.required),
    prenom: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    telephone: new FormControl('', Validators.required),
    adresse: new FormControl(''),
    eleves: new FormArray<FormGroup>([])
  });

  get eleves(): FormArray<FormGroup> {
    return this.form.get('eleves') as FormArray<FormGroup>;
  }

  constructor(private inscriptionService: InscriptionService) {}

  ngOnInit(): void {
    this.inscriptionService.getCours().subscribe(data => this.cours.set(data));
    this.ajouterEleve();
  }

  ajouterEleve(): void {
    this.eleves.push(new FormGroup({
      nom: new FormControl('', Validators.required),
      prenom: new FormControl('', Validators.required),
      dateNaissance: new FormControl(''),
    }));
    this.selectedCours.update(list => [...list, new Set<number>()]);
  }

  supprimerEleve(i: number): void {
    this.eleves.removeAt(i);
    this.selectedCours.update(list => list.filter((_, idx) => idx !== i));
  }

  eleveAsGroup(i: number): FormGroup {
    return this.eleves.at(i) as FormGroup;
  }

  toggleCours(eleveIndex: number, coursId: number): void {
    this.selectedCours.update(list => {
      const newList = [...list];
      const set = new Set(newList[eleveIndex]);
      if (set.has(coursId)) set.delete(coursId); else set.add(coursId);
      newList[eleveIndex] = set;
      return newList;
    });
  }

  isCoursSelected(eleveIndex: number, coursId: number): boolean {
    return this.selectedCours()[eleveIndex]?.has(coursId) ?? false;
  }

  soumettre(): void {
    this.erreurMessage.set('');
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.value;
    const payload = {
      nom: v.nom!,
      prenom: v.prenom!,
      email: v.email!,
      telephone: v.telephone!,
      adresse: v.adresse ?? '',
      eleves: this.eleves.controls.map((ctrl, i) => ({
        nom: ctrl.value.nom,
        prenom: ctrl.value.prenom,
        dateNaissance: ctrl.value.dateNaissance || null,
        coursIds: [...this.selectedCours()[i]],
      }))
    };

    this.enCours.set(true);
    this.inscriptionService.soumettrePublique(payload).subscribe({
      next: () => {
        this.succes.set(true);
        this.enCours.set(false);
      },
      error: (err) => {
        this.erreurMessage.set(err.error?.erreur ?? 'Une erreur est survenue. Veuillez réessayer.');
        this.enCours.set(false);
      }
    });
  }
}
