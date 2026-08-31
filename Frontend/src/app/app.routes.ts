import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { InscriptionComponent } from './features/inscription/inscription.component';
import { ArchivesComponent } from './features/archives/archives.component';
import { LoginComponent } from './features/admin/login/login.component';
import { AdminComponent } from './features/admin/admin.component';
import { authGuard } from './core/guards/auth.guard';
import { AgendaAdminComponent } from './features/admin/agenda-admin/agenda-admin.component';
import { InscritsAdminComponent } from './features/admin/inscrits-admin/inscrits-admin.component';
import { EvenementsAdminComponent } from './features/admin/evenements-admin/evenements-admin.component';
import { ArchivesAdminComponent } from './features/admin/archives-admin/archives-admin.component';
import { AgendaComponent } from './features/agenda/agenda.component';
import { ContactComponent } from './features/contact/contact.component';
import { ParametresAdminComponent } from './features/admin/parametres-admin/parametres-admin.component';
import { UtilisateursAdminComponent } from './features/admin/utilisateurs-admin/utilisateurs-admin.component';
import { MotDePasseOublieComponent } from './features/admin/mot-de-passe-oublie/mot-de-passe-oublie.component';
import { ReinitialiserMotDePasseComponent } from './features/admin/reinitialiser-mot-de-passe/reinitialiser-mot-de-passe.component';

export const routes: Routes = [
    { path: '', component: HomeComponent},
    { path: 'agenda', component: AgendaComponent},
    { path: 'inscription', component: InscriptionComponent},
    { path: 'archives', component: ArchivesComponent},
    { path: 'contact', component: ContactComponent},
    { path: 'login', component: LoginComponent},
    { path: 'mot-de-passe-oublie', component: MotDePasseOublieComponent },
    { path: 'reinitialiser-mot-de-passe', component: ReinitialiserMotDePasseComponent },
    {
        path: 'admin',
        component: AdminComponent,
        canActivate:[authGuard],
        children:[
            { path: '', redirectTo: 'planning', pathMatch: 'full'},
            { path: 'planning', component: AgendaAdminComponent},
            { path: 'evenements', component: EvenementsAdminComponent},
            { path: 'inscrits', component: InscritsAdminComponent},
            { path: 'archives', component: ArchivesAdminComponent},
            { path: 'parametres', component: ParametresAdminComponent},
            { path: 'utilisateurs', component: UtilisateursAdminComponent}
        ]
    },
    { path: '**', redirectTo:''}
];
