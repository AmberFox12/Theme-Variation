import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ParametreService } from '../../core/services/parametre.service';
import { Parametre } from '../../core/models/parametre.model';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-contact',
  imports: [ReactiveFormsModule],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.scss'
})
export class ContactComponent implements OnInit {
  parametre = signal<Parametre | null>(null);
  envoi = signal<'idle' | 'envoi' | 'ok' | 'erreur'>('idle');

  carteUrl = signal<SafeResourceUrl | null>(null);

  form = new FormGroup({
    nom:       new FormControl('', Validators.required),
    email:     new FormControl('', [Validators.required, Validators.email]),
    telephone: new FormControl(''),
    message:   new FormControl('', Validators.required),
  });

  constructor(
    private parametreService: ParametreService,
    private http: HttpClient,
    private sanitizer: DomSanitizer
  ) {}

  private static readonly HOTES_CARTE_AUTORISES = [
    'https://www.google.com/maps',
    'https://maps.google.com',
    'https://www.google.fr/maps',
  ];

  private static readonly CARTE_PAR_DEFAUT =
    `https://maps.google.com/maps?q=${encodeURIComponent("338 route de Francheville 27130 Verneuil d'Avre et d'Iton")}&output=embed&hl=fr`;

  ngOnInit(): void {
    this.parametreService.get().subscribe(p => {
      this.parametre.set(p);
      let url = p.carteEmbedUrl ?? null;
      // Accepte aussi le code iframe complet : extrait l'URL du src="..."
      if (url?.trim().startsWith('<iframe')) {
        const match = url.match(/src="([^"]+)"/);
        url = match ? match[1] : null;
      }
      // On ne fait confiance qu'à une URL Google Maps : ce champ est ensuite
      // rendu de confiance (bypassSecurityTrustResourceUrl), donc toute autre
      // valeur serait une porte ouverte à du XSS pour tous les visiteurs.
      const estAutorisee = !!url && ContactComponent.HOTES_CARTE_AUTORISES.some(hote => url!.startsWith(hote));
      url = estAutorisee ? url! : ContactComponent.CARTE_PAR_DEFAUT;
      this.carteUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(url));
    });
  }

  envoyer(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.envoi.set('envoi');
    this.http.post('/api/contact', this.form.value).subscribe({
      next: () => { this.envoi.set('ok'); this.form.reset(); },
      error: () => this.envoi.set('erreur'),
    });
  }
}
