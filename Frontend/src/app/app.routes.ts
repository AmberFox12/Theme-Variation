import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { InscriptionComponent } from './features/inscription/inscription.component';
import { LoginComponent } from './features/admin/login/login.component';
import { AdminComponent } from './features/admin/admin.component';
import { authGuard } from './core/guards/auth.guard';
import { AgendaAdminComponent } from './features/admin/agenda-admin/agenda-admin.component';
import { InscritsAdminComponent } from './features/admin/inscrits-admin/inscrits-admin.component';
import { EvenementsAdminComponent } from './features/admin/evenements-admin/evenements-admin.component';
import { AgendaComponent } from './features/agenda/agenda.component';

export const routes: Routes = [
    { path: '', component: HomeComponent},
    { path: 'agenda', component: AgendaComponent},
    { path: 'inscription', component: InscriptionComponent},
    { path: 'login', component: LoginComponent},
    {
        path: 'admin',
        component: AdminComponent,
        canActivate:[authGuard],
        children:[
            { path: '', redirectTo: 'planning', pathMatch: 'full'},
            { path: 'planning', component: AgendaAdminComponent},
            { path: 'evenements', component: EvenementsAdminComponent},
            { path: 'inscrits', component: InscritsAdminComponent}
        ]
    },
    { path: '**', redirectTo:''}
];
