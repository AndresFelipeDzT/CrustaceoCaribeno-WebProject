import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { of, forkJoin } from 'rxjs';
import { concatMap, map, catchError } from 'rxjs/operators';
import { UserService } from './services/user.service';
import { PostService } from './services/post.service';
import { CommentService } from './services/comment.service';
import { User } from './models/user.model';
import { Post } from './models/post.model';
import { UserCardComponent } from './components/user-card/user-card.component';
import { PostListComponent } from './components/post-list/post-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, UserCardComponent, PostListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  // Inyección de servicios para cada tabla/entidad (Arquitectura solicitada)
  private userService = inject(UserService);
  private postService = inject(PostService);
  private commentService = inject(CommentService);

  // Sección 1: Campo de búsqueda vinculado al formulario
  usernameBuscado: string = 'emilys';

  // Datos que se enviarán con @Input a los componentes hijos (Secciones 2 y 3)
  usuarioEncontrado: User | null = null;
  postsDelUsuario: Post[] = [];

  // Estados de la interfaz
  cargando: boolean = false;
  mensajeError: string = '';
  busquedaRealizada: boolean = false;

  /**
   * Consulta encadenada usando operadores de RxJS (map, concatMap, forkJoin):
   * 1. Consulta el usuario por 'username' en UserService.
   * 2. Si no existe, corta el flujo e informa al usuario.
   * 3. Si existe, usa 'concatMap' para esperar y consultar todos sus posts en PostService.
   * 4. Para cada post obtenido, consulta sus comentarios en paralelo con 'forkJoin' usando CommentService.
   * 5. Al finalizar toda la cadena reactiva, actualiza el estado y pasa los datos por @Input.
   */
  buscarUsuario(): void {
    const username = this.usernameBuscado.trim();
    if (!username) {
      this.mensajeError = 'Por favor ingrese un nombre de usuario para buscar.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.busquedaRealizada = true;
    this.usuarioEncontrado = null;
    this.postsDelUsuario = [];

    // Inicio de la cadena reactiva
    this.userService.getUserByUsername(username)
      .pipe(
        concatMap(user => {
          // Si el usuario no existe en la API, emitimos resultado nulo y cortamos
          if (!user) {
            this.mensajeError = `El nombre de usuario "${username}" no existe en el sistema.`;
            return of({ user: null, posts: [] });
          }

          // Si el usuario existe, consultamos sus posts usando su userId
          return this.postService.getPostsByUserId(user.id).pipe(
            concatMap(posts => {
              // Si el usuario no tiene posts, retornamos el usuario con lista vacía de posts
              if (!posts || posts.length === 0) {
                return of({ user, posts: [] });
              }

              // Para cada post, disparamos la consulta de sus comentarios y los fusionamos
              const postsConComentarios$ = posts.map(post =>
                this.commentService.getCommentsByPostId(post.id).pipe(
                  map(comments => ({
                    ...post,
                    comments: comments || []
                  })),
                  // Si falla la consulta de comentarios de un post, continuamos sin romper el flujo
                  catchError(() => of({ ...post, comments: [] }))
                )
              );

              // forkJoin espera a que se resuelvan las peticiones de comentarios de TODOS los posts
              return forkJoin(postsConComentarios$).pipe(
                map(postsCompletos => ({ user, posts: postsCompletos }))
              );
            }),
            catchError(err => {
              console.error('Error al obtener los posts:', err);
              return of({ user, posts: [] });
            })
          );
        }),
        catchError(err => {
          console.error('Error general en la consulta de usuario:', err);
          this.mensajeError = 'Ocurrió un error al consultar la API de DummyJSON.';
          return of({ user: null, posts: [] });
        })
      )
      .subscribe({
        next: ({ user, posts }) => {
          if (user) {
            this.usuarioEncontrado = user;
            this.postsDelUsuario = posts;
          } else {
            this.usuarioEncontrado = null;
            this.postsDelUsuario = [];
          }
          this.cargando = false;
        },
        error: () => {
          this.mensajeError = 'Error inesperado en la suscripción.';
          this.cargando = false;
        }
      });
  }

  // Helper para probar rápidamente con botones de ejemplo
  seleccionarEjemplo(username: string): void {
    this.usernameBuscado = username;
    this.buscarUsuario();
  }
}
