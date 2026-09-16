import { Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { IncidentsComponent } from './components/incidents/incidents.component';
import { StatsComponent } from './components/stats/stats.component';
import { AuthenticatedLayoutComponent } from './components/layout/authenticated-layout.component';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'login' },
	{ path: 'login', component: LoginComponent },
	{
		path: '',
		component: AuthenticatedLayoutComponent,
		canActivate: [authGuard],
		children: [
			{ path: 'incidents', component: IncidentsComponent },
			{ path: 'stats', component: StatsComponent }
		]
	},
	{ path: '**', redirectTo: 'login' }
];
