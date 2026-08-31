import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { switchMap, of } from 'rxjs';
import { ArchivesService, BACKEND_BASE } from '../../../core/services/archives.service';
import { Historique } from '../../../core/models/historique.model';
import { Spectacle } from '../../../core/models/spectacle.model';

@Component({
  selector: 'app-archives-admin',
  imports: [ReactiveFormsModule],
  templateUrl: './archives-admin.component.html',
  styleUrl: './archives-admin.component.scss'
})
export class ArchivesAdminComponent implements OnInit {

  readonly backendBase = BACKEND_BASE;
  onglet = signal<'historique' | 'spectacles'>('historique');

  // ── Historique ──
  historique = signal<Historique[]>([]);
  editHistorique = signal<Historique | null>(null);
  panneauHistorique = signal(false);

  formHistorique = new FormGroup({
    annee:       new FormControl('', Validators.required),
    titre:       new FormControl('', Validators.required),
    description: new FormControl(''),
    ordre:       new FormControl(0),
  });

  // ── Spectacles ──
  spectacles = signal<Spectacle[]>([]);
  editSpectacle = signal<Spectacle | null>(null);
  panneauSpectacle = signal(false);
  imageFile = signal<File | null>(null);
  imagePreview = signal<string | null>(null);
  imageErreur = signal<string | null>(null);

  readonly STATUTS_SPECTACLE = [
    { value: 'A_VENIR', label: 'À venir' },
    { value: 'PASSE',   label: 'Passé'   },
  ];

  formSpectacle = new FormGroup({
    titre:       new FormControl('', Validators.required),
    annee:       new FormControl('', Validators.required),
    lieu:        new FormControl(''),
    description: new FormControl(''),
    statut:      new FormControl('A_VENIR', Validators.required),
  });

  constructor(private archivesService: ArchivesService) {}

  ngOnInit(): void {
    this.chargerHistorique();
    this.chargerSpectacles();
  }

  chargerHistorique(): void {
    this.archivesService.getHistorique().subscribe(data => this.historique.set(data));
  }

  chargerSpectacles(): void {
    this.archivesService.getSpectacles().subscribe(data => this.spectacles.set(data));
  }

  // ── Actions Historique ──

  ouvrirHistorique(h?: Historique): void {
    this.editHistorique.set(h ?? null);
    this.formHistorique.reset(h
      ? { annee: h.annee, titre: h.titre, description: h.description, ordre: h.ordre }
      : { annee: '', titre: '', description: '', ordre: this.historique().length }
    );
    this.panneauHistorique.set(true);
  }

  sauvegarderHistorique(): void {
    if (this.formHistorique.invalid) return;
    const v = this.formHistorique.value;
    const data = { annee: v.annee!, titre: v.titre!, description: v.description ?? '', ordre: v.ordre ?? 0 };
    const edit = this.editHistorique();

    const op = edit
      ? this.archivesService.modifierHistorique(edit.id, data)
      : this.archivesService.creerHistorique(data);

    op.subscribe(() => { this.chargerHistorique(); this.panneauHistorique.set(false); });
  }

  supprimerHistorique(h: Historique): void {
    if (!confirm(`Supprimer "${h.titre}" ?`)) return;
    this.archivesService.supprimerHistorique(h.id).subscribe(() => this.chargerHistorique());
  }

  // ── Actions Spectacles ──

  ouvrirSpectacle(s?: Spectacle): void {
    this.editSpectacle.set(s ?? null);
    this.imageFile.set(null);
    this.imagePreview.set(s?.imageUrl ? `${this.backendBase}${s.imageUrl}` : null);
    this.imageErreur.set(null);
    this.formSpectacle.reset(s
      ? { titre: s.titre, annee: s.annee, lieu: s.lieu, description: s.description, statut: s.statut }
      : { titre: '', annee: '', lieu: '', description: '', statut: 'A_VENIR' }
    );
    this.panneauSpectacle.set(true);
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

  sauvegarderSpectacle(): void {
    if (this.formSpectacle.invalid) return;
    const v = this.formSpectacle.value;
    const data = { titre: v.titre!, annee: v.annee!, lieu: v.lieu ?? '', description: v.description ?? '', statut: v.statut! };
    const edit = this.editSpectacle();
    const file = this.imageFile();

    const op = edit
      ? this.archivesService.modifierSpectacle(edit.id, data)
      : this.archivesService.creerSpectacle(data);

    op.pipe(
      switchMap(spectacle => file
        ? this.archivesService.uploaderImageSpectacle(spectacle.id, file)
        : of(spectacle)
      )
    ).subscribe(() => {
      this.chargerSpectacles();
      this.panneauSpectacle.set(false);
      this.imageFile.set(null);
      this.imagePreview.set(null);
    });
  }

  supprimerSpectacle(s: Spectacle): void {
    if (!confirm(`Supprimer le spectacle "${s.titre}" ?`)) return;
    this.archivesService.supprimerSpectacle(s.id).subscribe(() => this.chargerSpectacles());
  }

  labelStatut(statut: string): string {
    return statut === 'A_VENIR' ? 'À venir' : 'Passé';
  }
}
