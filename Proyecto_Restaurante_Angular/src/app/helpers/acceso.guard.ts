import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AdministradorService } from '../service/administrador.service';
import { ClienteService } from '../service/cliente.service';

export const requiereAdministrador: CanActivateFn = (_route, state) => {
  const router = inject(Router);
  const administradorService = inject(AdministradorService);
  const login = router.createUrlTree(['/admin']);
  if (!localStorage.getItem('token')) return login;

  return administradorService.getAdministradorActual().pipe(
    map(perfil => {
      const destino = router.parseUrl(state.url);
      if (destino.queryParams['idAdministrador'] === String(perfil.idAdministrador)) return true;
      destino.queryParams = { ...destino.queryParams, idAdministrador: String(perfil.idAdministrador) };
      return destino;
    }),
    catchError(() => of(login))
  );
};

export const cargarPerfilCliente: CanActivateFn = route => {
  const clienteService = inject(ClienteService);
  const router = inject(Router);
  const sinPerfil = route.data['requiereCliente'] ? router.createUrlTree(['/login']) : true;
  return clienteService.getClienteActual().pipe(
    map(cliente => cliente ? true : sinPerfil),
    catchError(() => of(sinPerfil))
  );
};
