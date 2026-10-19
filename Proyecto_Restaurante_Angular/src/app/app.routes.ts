import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ComidasTarjetasPageComponent } from './pages/comidas-tarjetas-page/comidas-tarjetas-page.component';
import { ComidaFormComponent } from './pages/comida-form/comida-form.component';
import { ComidaDetailComponent } from './pages/comida-detail/comida-detail.component';
import { ComidasTablaPageComponent } from './pages/comidas-tabla-page/comidas-tabla-page.component';
import { ClienteFormComponent } from './pages/cliente-form/cliente-form.component';
import { ClienteDetailComponent } from './pages/cliente-detail/cliente-detail.component';
import { LoginComponent } from './pages/login/login.component';
import { ClienteProfileComponent } from './pages/cliente-profile/cliente-profile.component';
import { PedidosAdminComponent } from './pages/pedidos-admin/pedidos-admin.component';
import { CarritoPageComponent } from './pages/carrito-page/carrito-page.component';
import { DomiciliariosPageComponent } from './pages/domiciliarios-page/domiciliarios-page.component';
import { DomiciliarioFormComponent } from './pages/domiciliario-form/domiciliario-form.component';
import { OperadoresPageComponent } from './pages/operadores-page/operadores-page.component';
import { OperadorFormComponent } from './pages/operador-form/operador-form.component';
import { AdicionalesTablaPageComponent } from './pages/adicionales-tabla-page/adicionales-tabla-page.component';
import { AdicionalFormComponent } from './pages/adicional-form/adicional-form.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent, title: 'Crustáceo Caribeño | Inicio' },
  { path: 'home', component: HomePageComponent, title: 'Crustáceo Caribeño | Inicio' },

  { path: 'login', component: LoginComponent, title: 'Login | Crustáceo Caribeño' },
  { path: 'admin', component: LoginComponent, title: 'Acceso administrador | Crustáceo Caribeño' },
  { path: 'operador', component: LoginComponent, title: 'Acceso operador | Crustáceo Caribeño' },
  { path: 'operador/pedidos', component: PedidosAdminComponent, title: 'Pedidos | Portal de operador' },
  { path: 'operador/pedidos/:id', component: PedidosAdminComponent, title: 'Detalle del pedido | Portal de operador' },
  { path: 'perfil', component: ClienteProfileComponent, title: 'Mi perfil | Crustáceo Caribeño' },
  { path: 'carrito', component: CarritoPageComponent, title: 'Mi carrito | Crustáceo Caribeño' },
  { path: 'pedidos', component: PedidosAdminComponent, title: 'Pedidos | Crustáceo Caribeño' },
  { path: 'pedidos/:id', component: PedidosAdminComponent, title: 'Detalle del pedido' },
  { path: 'domiciliarios/add', component: DomiciliarioFormComponent, title: 'Registrar domiciliario' },
  { path: 'domiciliarios/update/:id', component: DomiciliarioFormComponent, title: 'Editar domiciliario' },
  { path: 'domiciliarios', component: DomiciliariosPageComponent, title: 'Domiciliarios | Crustáceo Caribeño' },
  { path: 'operadores/add', component: OperadorFormComponent, title: 'Registrar operador' },
  { path: 'operadores/update/:id', component: OperadorFormComponent, title: 'Editar operador' },
  { path: 'operadores', component: OperadoresPageComponent, title: 'Operadores | Crustáceo Caribeño' },

  // Rutas de Comidas
  { path: 'comidas/tarjetas', component: ComidasTarjetasPageComponent, title: 'Crustáceo Caribeño | Menú' },
  { path: 'comidas/tabla/add', component: ComidaFormComponent, title: 'Agregar plato' },
  { path: 'comidas/tabla/update/:id', component: ComidaFormComponent, title: 'Editar plato' },
  { path: 'comidas/tabla', component: ComidasTablaPageComponent, title: 'Crustáceo Caribeño | Menú en tabla' },
  { path: 'comidas/detalle/:id', component: ComidaDetailComponent, title: 'Detalle de comida' },

  // Rutas de Adicionales
  { path: 'adicionales/tabla/add', component: AdicionalFormComponent, title: 'Agregar adicional' },
  { path: 'adicionales/tabla/update/:id', component: AdicionalFormComponent, title: 'Editar adicional' },
  { path: 'adicionales/tabla', component: AdicionalesTablaPageComponent, title: 'Adicionales | Crustáceo Caribeño' },

  // Rutas de Clientes (CRUD)
  { path: 'clientes', redirectTo: '' },
  { path: 'clientes/add', component: ClienteFormComponent, title: 'Registrar cliente' },
  { path: 'clientes/update/:id', component: ClienteFormComponent, title: 'Editar cliente' },
  { path: 'clientes/detalle/:id', component: ClienteDetailComponent, title: 'Detalle de cliente' },

  { path: '**', redirectTo: '' }
];
