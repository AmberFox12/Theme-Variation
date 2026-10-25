import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { ParametreService } from '../../../core/services/parametre.service';

@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {
  private parametreService = inject(ParametreService);
  parametre = toSignal(this.parametreService.get());

  readonly annee = new Date().getFullYear();

  readonly INSTAGRAM_DEFAUT = 'https://www.instagram.com/associationthemeetvariations';
  readonly FACEBOOK_DEFAUT  = 'https://www.facebook.com/share/1EXKRf9bcT/';
}
