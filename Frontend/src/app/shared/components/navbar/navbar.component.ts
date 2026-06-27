import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { from } from 'rxjs';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {}
