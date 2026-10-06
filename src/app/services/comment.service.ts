import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'https://dummyjson.com/comments';

  /** Obtiene los comentarios de la API */
  getAllComments(): Observable<Comment[]> {
    return this.http.get<{ comments: Comment[] }>(this.baseUrl).pipe(
      map(response => response?.comments || [])
    );
  }

  /**
   * Obtiene los comentarios de un post específico
   */
  getCommentsByPostId(postId: number): Observable<Comment[]> {
    const url = `${this.baseUrl}/post/${postId}`;
    return this.http.get<{ comments: Comment[] }>(url).pipe(
      map(response => response?.comments || [])
    );
  }
}
