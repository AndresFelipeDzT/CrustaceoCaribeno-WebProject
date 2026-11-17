import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const PROFILE_URLS = [
  'http://localhost:8080/api/clientes/details',
  'http://localhost:8080/api/operadores/details',
  'http://localhost:8080/api/administradores/details'
];
const API_URL = 'http://localhost:8080/api/';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('token');
    const esRutaProtegida = request.url.startsWith(API_URL) && (
      PROFILE_URLS.includes(request.url)
      || (request.method === 'GET' && request.url === `${API_URL}clientes`)
      || /^clientes\/\d+$/.test(request.url.slice(API_URL.length))
      || request.url.startsWith(`${API_URL}carritos/`)
      || request.url === `${API_URL}administradores`
      || (request.url.startsWith(`${API_URL}administradores/`) && request.url !== `${API_URL}administradores/login`)
      || request.url === `${API_URL}operadores`
      || (request.url.startsWith(`${API_URL}operadores/`) && request.url !== `${API_URL}operadores/login`)
      || request.url === `${API_URL}pedidos`
      || request.url.startsWith(`${API_URL}pedidos/`)
      || request.url === `${API_URL}domiciliarios`
      || request.url.startsWith(`${API_URL}domiciliarios/`)
      || (request.method !== 'GET' && /^(adicionales|productos|categorias)(?:\/|\?|$)/.test(request.url.slice(API_URL.length)))
    );
    if (token && esRutaProtegida) {
      const requestConToken = request.clone({
        headers: request.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(requestConToken);
    }
    return next.handle(request);
  }
}
