import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ComidasTarjetasPageComponent } from './pages/comidas-tarjetas-page/comidas-tarjetas-page.component';
import { ComidaFormComponent } from './pages/comida-form/comida-form.component';
import { ComidaDetailComponent } from './pages/comida-detail/comida-detail.component';
import { ComidasTablaPageComponent } from './pages/comidas-tabla-page/comidas-tabla-page.component';
import { ClientesTablaPageComponent } from './pages/clientes-tabla-page/clientes-tabla-page.component';
import { ClienteFormComponent } from './pages/cliente-form/cliente-form.component';
import { ClienteDetailComponent } from './pages/cliente-detail/cliente-detail.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent, title: 'Crustáceo Caribeño | Inicio' },
  { path: 'home', component: HomePageComponent, title: 'Crustáceo Caribeño | Inicio' },

  // Rutas de Comidas
  { path: 'comidas/tarjetas', component: ComidasTarjetasPageComponent, title: 'Crustáceo Caribeño | Menú' },
  { path: 'comidas/tabla/add', component: ComidaFormComponent, title: 'Agregar plato' },
  { path: 'comidas/tabla/update/:id', component: ComidaFormComponent, title: 'Editar plato' },
  { path: 'comidas/tabla', component: ComidasTablaPageComponent, title: 'Crustáceo Caribeño | Menú en tabla' },
  { path: 'comidas/detalle/:id', component: ComidaDetailComponent, title: 'Detalle de comida' },

  // Rutas de Clientes (CRUD)
  { path: 'clientes', component: ClientesTablaPageComponent, title: 'Crustáceo Caribeño | Clientes' },
  { path: 'clientes/tabla', redirectTo: 'clientes' },
  { path: 'clientes/add', component: ClienteFormComponent, title: 'Registrar cliente' },
  { path: 'clientes/tabla/add', redirectTo: 'clientes/add' },
  { path: 'clientes/update/:id', component: ClienteFormComponent, title: 'Editar cliente' },
  { path: 'clientes/tabla/update/:id', redirectTo: 'clientes/update/:id' },
  { path: 'clientes/detalle/:id', component: ClienteDetailComponent, title: 'Detalle de cliente' },

  { path: '**', redirectTo: '' }
];
