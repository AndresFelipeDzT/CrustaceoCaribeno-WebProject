import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, of } from 'rxjs';
import { concatMap, map, catchError, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
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
export class App implements OnInit {
  // Inyección de servicios para cada entidad (Arquitectura solicitada)
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

  // Sujeto reactivo para el buscador (Patrón de búsqueda visto en clase)
  private searchSubject = new Subject<string>();

  ngOnInit(): void {
    this.iniciarBuscadorReactivo();
  }

  /**
   * Buscador Reactivo usando operadores de la clase:
   * - debounceTime(350): Espera a que el usuario termine de escribir antes de consultar.
   * - distinctUntilChanged(): Evita consultas duplicadas si se busca dos veces el mismo texto.
   * - switchMap: Cancela peticiones previas en vuelo si se realiza una nueva búsqueda.
   * - concatMap: Encadena secuencialmente Usuario -> Posts -> Comentarios.
   * - map: Asocia en memoria los comentarios a sus posts correspondientes.
   * - Único error contemplado: Usuario no encontrado en el sistema.
   */
  private iniciarBuscadorReactivo(): void {
    this.searchSubject.pipe(
      map(term => term.trim()),
      debounceTime(350),
      distinctUntilChanged(),
      switchMap(username => {
        if (!username) {
          this.cargando = false;
          this.usuarioEncontrado = null;
          this.postsDelUsuario = [];
          this.mensajeError = '';
          this.busquedaRealizada = false;
          return of(null);
        }

        this.cargando = true;
        this.mensajeError = '';
        this.busquedaRealizada = true;
        this.usuarioEncontrado = null;
        this.postsDelUsuario = [];

        return this.userService.getUserByUsername(username).pipe(
          concatMap(user => {
            // Único caso de error requerido por la rúbrica: cuando el usuario no existe
            if (!user) {
              this.mensajeError = `El nombre de usuario "${username}" no existe en el sistema.`;
              return of({ user: null, posts: [] });
            }

            // Si el usuario existe, consultamos sus posts
            return this.postService.getPostsByUserId(user.id).pipe(
              concatMap(posts => {
                if (!posts || posts.length === 0) {
                  return of({ user, posts: [] });
                }

                // Consultamos los comentarios en una sola petición y los vinculamos con map
                return this.commentService.getAllComments().pipe(
                  map(allComments => {
                    const postsConComentarios = posts.map(post => ({
                      ...post,
                      comments: allComments.filter(c => c.postId === post.id)
                    }));
                    return { user, posts: postsConComentarios };
                  }),
                  catchError(() => of({ user, posts }))
                );
              }),
              catchError(() => of({ user, posts: [] }))
            );
          }),
          catchError(() => of({ user: null, posts: [] }))
        );
      })
    ).subscribe(resultado => {
      if (resultado) {
        this.usuarioEncontrado = resultado.user;
        this.postsDelUsuario = resultado.posts;
      }
      this.cargando = false;
    });
  }

  // Evento al teclear en el campo de búsqueda
  onSearchInput(valor: string): void {
    this.usernameBuscado = valor;
    this.searchSubject.next(valor);
  }

  // Evento al enviar el formulario o hacer clic en "Buscar"
  buscarUsuario(): void {
    this.searchSubject.next(this.usernameBuscado);
  }

  // Accesos directos para probar usuarios
  seleccionarEjemplo(username: string): void {
    this.usernameBuscado = username;
    this.searchSubject.next(username);
  }

  seleccionarUsuarioEjemplo(username: string): void {
    this.seleccionarEjemplo(username);
  }
}
