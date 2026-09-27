import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ComidasTarjetasPageComponent } from './pages/comidas-tarjetas-page/comidas-tarjetas-page.component';
import { ComidaFormComponent } from './pages/comida-form/comida-form.component';
import { ComidaDetailComponent } from './pages/comida-detail/comida-detail.component';
import { ComidasTablaPageComponent } from './pages/comidas-tabla-page/comidas-tabla-page.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent, title: 'Crustáceo Caribeño | Inicio' },
  { path: 'home', component: HomePageComponent, title: 'Crustáceo Caribeño | Inicio' },
  { path: 'comidas/tarjetas', component: ComidasTarjetasPageComponent, title: 'Crustáceo Caribeño | Menú' },
  { path: 'comidas/tabla/add', component: ComidaFormComponent, title: 'Agregar plato' },
  { path: 'comidas/tabla/update/:id', component: ComidaFormComponent, title: 'Editar plato' },
  { path: 'comidas/tabla', component: ComidasTablaPageComponent, title: 'Crustáceo Caribeño | Menú en tabla' },
  { path: 'comidas/detalle/:id', component: ComidaDetailComponent, title: 'Detalle de comida' },
  { path: '**', redirectTo: '' }
];
