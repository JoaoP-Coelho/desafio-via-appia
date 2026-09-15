import { Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { IncidentsComponent } from './components/incidents/incidents.component';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'login' },
	{ path: 'login', component: LoginComponent },
	{ path: 'incidents', component: IncidentsComponent },
	{ path: '**', redirectTo: 'login' }
];
