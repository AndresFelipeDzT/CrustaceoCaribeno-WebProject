import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, Observable, of } from 'rxjs';
import { AdministradorService } from '../service/administrador.service';
import { ClienteService } from '../service/cliente.service';
import { OperadorService } from '../service/operador.service';

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
