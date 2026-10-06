import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Comment, CommentResponse } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'https://dummyjson.com/comments';

  /**
   * Obtiene todos los comentarios asociados a un post según su postId
   */
  getCommentsByPostId(postId: number): Observable<Comment[]> {
    const url = `${this.baseUrl}/post/${postId}`;
    return this.http.get<CommentResponse>(url).pipe(
      map(response => response?.comments || [])
    );
  }
}
