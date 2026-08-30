import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ParametreService } from '../../../core/services/parametre.service';

@Component({
  selector: 'app-parametres-admin',
  imports: [ReactiveFormsModule],
  templateUrl: './parametres-admin.component.html',
  styleUrl: './parametres-admin.component.scss'
})
export class ParametresAdminComponent implements OnInit {
  sauvegarde = signal<'idle' | 'ok' | 'erreur'>('idle');

  form = new FormGroup({
    email:             new FormControl(''),
    telephone:         new FormControl(''),
    adresse:           new FormControl(''),
    instagram:         new FormControl(''),
    facebook:          new FormControl(''),
    emailNotification: new FormControl(''),
    carteEmbedUrl:     new FormControl(''),
  });

  constructor(private parametreService: ParametreService) {}

  ngOnInit(): void {
    this.parametreService.get().subscribe(p => {
      this.form.patchValue({
        email:             p.email ?? '',
        telephone:         p.telephone ?? '',
        adresse:           p.adresse ?? '',
        instagram:         p.instagram ?? '',
        facebook:          p.facebook ?? '',
        emailNotification: p.emailNotification ?? '',
        carteEmbedUrl:     p.carteEmbedUrl ?? '',
      });
    });
  }

  sauvegarder(): void {
    const v = this.form.value;
    this.parametreService.update({
      id: 1,
      email:             v.email || null,
      telephone:         v.telephone || null,
      adresse:           v.adresse || null,
      instagram:         v.instagram || null,
      facebook:          v.facebook || null,
      emailNotification: v.emailNotification || null,
      carteEmbedUrl:     v.carteEmbedUrl || null,
    }).subscribe({
      next: () => { this.sauvegarde.set('ok'); setTimeout(() => this.sauvegarde.set('idle'), 3000); },
      error: () => this.sauvegarde.set('erreur'),
    });
  }
}
