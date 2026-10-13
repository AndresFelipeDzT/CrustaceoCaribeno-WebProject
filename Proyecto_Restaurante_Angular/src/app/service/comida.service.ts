import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Adicional } from '../models/adicional.model';
import { Categoria } from '../models/categoria.model';
import { Producto } from '../models/producto.model';

@Injectable({ providedIn: 'root' })
export class ComidaService {
  private readonly apiUrl = 'http://localhost:8080/api/productos';
  private readonly categoriasUrl = 'http://localhost:8080/api/categorias';
  private readonly adicionalesUrl = 'http://localhost:8080/api/adicionales';

  constructor(private http: HttpClient) {}

  getComidas(incluirInactivos = false): Observable<Producto[]> {
    const params = incluirInactivos
      ? new HttpParams().set('incluirInactivos', 'true')
      : undefined;
    return this.http.get<Producto[]>(this.apiUrl, { params });
  }

  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.categoriasUrl);
  }

  getCategoriaByNombre(nombre: string): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.categoriasUrl}/nombre/${encodeURIComponent(nombre)}`);
  }

  deleteCategoria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.categoriasUrl}/${id}`);
  }

  getAdicionales(categoriaId?: number): Observable<Adicional[]> {
    const params = categoriaId === undefined
      ? undefined
      : new HttpParams().set('categoriaId', categoriaId);
    return this.http.get<Adicional[]>(this.adicionalesUrl, { params });
  }

  getAdicionalById(id: number): Observable<Adicional> {
    return this.http.get<Adicional>(`${this.adicionalesUrl}/${id}`);
  }

  addAdicional(adicional: Omit<Adicional, 'idAdicional'>): Observable<void> {
    return this.http.post<void>(this.adicionalesUrl, adicional);
  }

  updateAdicional(id: number, adicional: Omit<Adicional, 'idAdicional'>): Observable<void> {
    return this.http.put<void>(`${this.adicionalesUrl}/${id}`, adicional);
  }

  deleteAdicional(id: number): Observable<void> {
    return this.http.delete<void>(`${this.adicionalesUrl}/${id}`);
  }

  getComidaById(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  addComida(comida: Omit<Producto, 'idProducto'>): Observable<void> {
    return this.http.post<void>(this.apiUrl, comida);
  }

  updateComida(id: number, comida: Omit<Producto, 'idProducto'>): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, comida);
  }

  deleteComida(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activarComida(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/activar`, {});
  }
}
