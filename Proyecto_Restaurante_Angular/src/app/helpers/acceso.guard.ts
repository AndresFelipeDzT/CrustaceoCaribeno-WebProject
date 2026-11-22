import { inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { CanActivateFn, RedirectCommand, Router, UrlTree } from '@angular/router';
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
    () => operadorService.getOperadorActual().pipe(map(() => router.createUrlTree(['/operador/pedidos']))),
    () => administradorService.getAdministradorActual().pipe(map(() => router.createUrlTree(['/pedidos'])))
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
  return validarPortal('/admin', state.url, () =>
    administradorService.getAdministradorActual()
  );
};

export const requiereOperador: CanActivateFn = (_route, state) => {
  const operadorService = inject(OperadorService);
  return validarPortal('/operador', state.url, () =>
    operadorService.getOperadorActual()
  );
};

function validarPortal(rutaLogin: string, url: string, consultarPerfil: () => Observable<unknown>) {
  const router = inject(Router);
  const login = router.createUrlTree([rutaLogin]);
  if (!localStorage.getItem('token')) return login;

  return consultarPerfil().pipe(
    map(() => quitarParametros(router.parseUrl(url), 'idAdministrador', 'idOperador')),
    catchError(() => of(login))
  );
}

function quitarParametros(destino: UrlTree, ...nombres: string[]): true | RedirectCommand {
  if (!nombres.some(nombre => destino.queryParamMap.has(nombre))) return true;
  for (const nombre of nombres) delete destino.queryParams[nombre];
  return new RedirectCommand(destino, { replaceUrl: true });
}

export const quitarIdDelPerfil: CanActivateFn = (_route, state) =>
  quitarParametros(inject(Router).parseUrl(state.url), 'id');

export const cargarPerfilCliente: CanActivateFn = route => {
  const clienteService = inject(ClienteService);
  const router = inject(Router);
  const sinPerfil = route.data['requiereCliente'] ? router.createUrlTree(['/login']) : true;
  return clienteService.getClienteActual().pipe(
    map(cliente => cliente ? true : sinPerfil),
    catchError(() => of(sinPerfil))
  );
};
