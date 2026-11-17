import { inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, concatMap, defaultIfEmpty, EMPTY, filter, from, map, Observable, of, take, throwError } from 'rxjs';
import { AdministradorService } from '../service/administrador.service';
import { AuthService } from '../service/auth.service';
import { ClienteService } from '../service/cliente.service';
import { OperadorService } from '../service/operador.service';

export const redirigirDesdeInicio: CanActivateFn = () => {
  const token = localStorage.getItem('token');
  if (!token) return true;

  const router = inject(Router);
  const auth = inject(AuthService);
  const clienteService = inject(ClienteService);
  const operadorService = inject(OperadorService);
  const administradorService = inject(AdministradorService);

  const consultarDestinos = [
    () => clienteService.getClienteActual().pipe(map(cliente => cliente && router.createUrlTree(
      ['/comidas/tarjetas'], { queryParams: { id: cliente.idCliente } }
    ))),
    () => operadorService.getOperadorActual().pipe(map(operador => router.createUrlTree(
      ['/operador/pedidos'], { queryParams: { idOperador: operador.idOperador } }
    ))),
    () => administradorService.getAdministradorActual().pipe(map(administrador => router.createUrlTree(
      ['/pedidos'], { queryParams: { idAdministrador: administrador.idAdministrador } }
    )))
  ];

  return from(consultarDestinos).pipe(
    concatMap(consultar => consultar().pipe(
      catchError((error: HttpErrorResponse) => error.status === 403 || error.status === 404
        ? EMPTY
        : throwError(() => error))
    )),
    filter(destino => destino !== undefined),
    take(1),
    defaultIfEmpty(undefined),
    map(destino => {
      if (token !== localStorage.getItem('token')) return true;
      if (!destino) auth.cerrarSesion();
      return destino ?? true;
    }),
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && token === localStorage.getItem('token')) auth.cerrarSesion();
      return of(true);
    })
  );
};

export const requiereAdministrador: CanActivateFn = (_route, state) => {
  const administradorService = inject(AdministradorService);
  return validarPortal('/admin', 'idAdministrador', state.url, () =>
    administradorService.getAdministradorActual().pipe(map(perfil => perfil.idAdministrador))
  );
};

export const requiereOperador: CanActivateFn = (_route, state) => {
  const operadorService = inject(OperadorService);
  return validarPortal('/operador', 'idOperador', state.url, () =>
    operadorService.getOperadorActual().pipe(map(perfil => perfil.idOperador))
  );
};

function validarPortal(rutaLogin: string, parametroId: string, url: string, consultarId: () => Observable<number>) {
  const router = inject(Router);
  const login = router.createUrlTree([rutaLogin]);
  if (!localStorage.getItem('token')) return login;

  return consultarId().pipe(
    map(id => {
      const destino = router.parseUrl(url);
      if (destino.queryParams[parametroId] === String(id)) return true;
      destino.queryParams = { ...destino.queryParams, [parametroId]: String(id) };
      return destino;
    }),
    catchError(() => of(login))
  );
}

export const cargarPerfilCliente: CanActivateFn = route => {
  const clienteService = inject(ClienteService);
  const router = inject(Router);
  const sinPerfil = route.data['requiereCliente'] ? router.createUrlTree(['/login']) : true;
  return clienteService.getClienteActual().pipe(
    map(cliente => cliente ? true : sinPerfil),
    catchError(() => of(sinPerfil))
  );
};
