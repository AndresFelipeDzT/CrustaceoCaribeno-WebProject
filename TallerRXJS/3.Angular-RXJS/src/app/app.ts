import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { mergeMap, of } from 'rxjs';
import { User } from './models/user';
import { Post } from './models/post';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  // Inyección de HttpClient para realizar peticiones HTTP (retornan Observables)
  private http = inject(HttpClient);

  // URL base de la API pública JSONPlaceholder
  readonly rootURL: string = 'https://jsonplaceholder.typicode.com';

  // Campo vinculado con two-way binding [(ngModel)] al input del formulario
  txtUser: string = 'Bret';

  // Datos reactivos que se actualizarán al resolverse las peticiones
  usuario: User | null = null;
  publicacion: Post | null = null;
  cargando: boolean = false;
  mensajeError: string = '';

  /**
   * Peticiones encadenadas con RxJS usando 'mergeMap' (Tema central de la Clase 9):
   *
   * Problema sin RxJS / llamadas independientes:
   * Si realizamos http.get(users) y http.get(posts?userId=usuario.id) por separado,
   * la segunda petición se dispara antes de que la primera responda, 'usuario.id'
   * es null y la aplicación falla con una condición de carrera.
   *
   * Solución con RxJS:
   * 1. Realizamos la primera petición al endpoint de usuarios.
   * 2. Con .pipe(mergeMap(...)), cuando llega la respuesta del usuario, tomamos su ID.
   * 3. Retornamos la segunda petición HTTP que busca los posts de ese usuario específico.
   * 4. En el .subscribe final, recibimos el resultado del post y actualizamos la vista.
   */
  obtenerUsuarioYPosts(): void {
    const usernameBuscado = this.txtUser.trim();
    if (!usernameBuscado) {
      this.mensajeError = 'Por favor ingrese un nombre de usuario.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.usuario = null;
    this.publicacion = null;

    // 1. Primera petición: Buscar usuario por username
    this.http.get<User[]>(`${this.rootURL}/users?username=${usernameBuscado}`)
      .pipe(
        mergeMap((users: User[]) => {
          if (users && users.length > 0) {
            // Guardamos el usuario encontrado
            this.usuario = users[0];
            console.log('[RxJS Step 1] Usuario obtenido:', this.usuario);

            // 2. Segunda petición dependiente: buscar posts asociados al ID de este usuario
            return this.http.get<Post[]>(`${this.rootURL}/posts?userId=${this.usuario.id}`);
          } else {
            // Si el usuario no existe, limpiamos y retornamos un observable vacío
            this.usuario = null;
            this.mensajeError = `No se encontró ningún usuario con el username '${usernameBuscado}'.`;
            return of([]);
          }
        })
      )
      .subscribe({
        next: (posts: Post[]) => {
          if (posts && posts.length > 0) {
            // Mostramos la primera publicación (tal como en la clase)
            this.publicacion = posts[0];
            console.log('[RxJS Step 2] Post asociado obtenido:', this.publicacion);
          } else {
            this.publicacion = null;
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error al realizar las peticiones:', err);
          this.mensajeError = 'Ocurrió un error al consultar los datos del servidor.';
          this.cargando = false;
        }
      });
  }

  // Método auxiliar para probar rápidamente con los usernames de JSONPlaceholder
  seleccionarEjemplo(nombre: string): void {
    this.txtUser = nombre;
    this.obtenerUsuarioYPosts();
  }
}
