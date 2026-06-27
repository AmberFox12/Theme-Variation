import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { BilletterieComponent } from './features/billetterie/billetterie.component';
import { InscriptionComponent } from './features/inscription/inscription.component';
import { LoginComponent } from './features/admin/login/login.component';
import { AdminComponent } from './features/admin/admin.component';
import { authGuard } from './core/guards/auth.guard';
import { AgendaAdminComponent } from './features/admin/agenda-admin/agenda-admin.component';
import { InscritsAdminComponent } from './features/admin/inscrits-admin/inscrits-admin.component';
import { BilletterieAdminComponent } from './features/admin/billetterie-admin/billetterie-admin.component';
import { AgendaComponent } from './features/agenda/agenda.component';

export const routes: Routes = [
    { path: '', component: HomeComponent},
    { path: 'billetterie', component: BilletterieComponent},
    { path: 'agenda', component: AgendaComponent},
    { path: 'inscription', component: InscriptionComponent},
    { path: 'login', component: LoginComponent},
    { 
        path: 'admin',
        component: AdminComponent,
        canActivate:[authGuard],
        children:[
            { path: '', redirectTo: 'agenda', pathMatch: 'full'},
            { path: 'agenda', component: AgendaAdminComponent},
            { path: 'billeterie', component: BilletterieAdminComponent},
            { path: 'inscrits', component: InscritsAdminComponent}
        ]
    },
    { path: '**', redirectTo:''}
];
