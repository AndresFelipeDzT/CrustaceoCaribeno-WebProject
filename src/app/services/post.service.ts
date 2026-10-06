import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Post, PostResponse } from '../models/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'https://dummyjson.com/posts';

  /**
   * Obtiene todos los posts realizados por un usuario según su userId
   */
  getPostsByUserId(userId: number): Observable<Post[]> {
    const url = `${this.baseUrl}/user/${userId}`;
    return this.http.get<PostResponse>(url).pipe(
      map(response => response?.posts || [])
    );
  }

  /**
   * Obtiene un post específico por su ID
   */
  getPostById(postId: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/${postId}`);
  }
}
