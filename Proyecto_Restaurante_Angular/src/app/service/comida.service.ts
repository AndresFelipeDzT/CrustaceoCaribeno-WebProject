import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Adicional, AdicionalRequest } from '../models/adicional.model';
import { Categoria } from '../models/categoria.model';
import { Producto, ProductoDTO } from '../models/producto.model';

@Injectable({ providedIn: 'root' })
/** Centraliza las llamadas de productos, categorías y adicionales. */
export class ComidaService {
  private readonly apiUrl = 'http://localhost:8080/api/productos';
  private readonly categoriasUrl = 'http://localhost:8080/api/categorias';
  private readonly adicionalesUrl = 'http://localhost:8080/api/adicionales';

  constructor(private http: HttpClient) {}

  getComidas(incluirInactivos = false): Observable<Producto[]> {
    const params = incluirInactivos
      ? new HttpParams().set('incluirInactivos', 'true')
      : undefined;
    return this.http.get<ProductoDTO[]>(this.apiUrl, { params }).pipe(
      map(productos => productos.map(producto => this.mapProducto(producto)))
    );
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

  getAdicionales(categoriaId?: number, incluirInactivos = false): Observable<Adicional[]> {
    let params = new HttpParams();
    if (categoriaId !== undefined) params = params.set('categoriaId', categoriaId);
    if (incluirInactivos) params = params.set('incluirInactivos', 'true');
    return this.http.get<Adicional[]>(this.adicionalesUrl, { params });
  }

  getAdicionalById(id: number): Observable<Adicional> {
    return this.http.get<Adicional>(`${this.adicionalesUrl}/${id}`);
  }

  addAdicional(adicional: AdicionalRequest): Observable<void> {
    return this.http.post<void>(this.adicionalesUrl, adicional);
  }

  updateAdicional(id: number, adicional: AdicionalRequest): Observable<void> {
    return this.http.put<void>(`${this.adicionalesUrl}/${id}`, adicional);
  }

  deleteAdicional(id: number): Observable<void> {
    return this.http.delete<void>(`${this.adicionalesUrl}/${id}`);
  }

  getComidaById(id: number): Observable<Producto> {
    return this.http.get<ProductoDTO>(`${this.apiUrl}/${id}`).pipe(
      map(producto => this.mapProducto(producto))
    );
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

  private mapProducto(producto: ProductoDTO): Producto {
    return {
      idProducto: producto.idProducto,
      nombre: producto.nombre,
      precio: producto.precio,
      descripcion: producto.descripcion,
      imagenURL: producto.imagenURL,
      activo: producto.activo,
      categoria: producto.idCategoria == null
        ? null
        : { idCategoria: producto.idCategoria, nombre: producto.nombreCategoria ?? '' }
    };
  }
}
