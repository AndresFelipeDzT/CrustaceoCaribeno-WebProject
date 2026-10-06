import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'https://dummyjson.com/users';

  /**
   * Consulta un usuario por su nombre de usuario (username)
   * Devuelve un Observable con el User encontrado o null si no existe.
   */
  getUserByUsername(username: string): Observable<User | null> {
    const url = `${this.baseUrl}/filter?key=username&value=${encodeURIComponent(username.trim())}`;
    return this.http.get<{ users: User[] }>(url).pipe(
      map(response => {
        if (response && response.users && response.users.length > 0) {
          return response.users[0];
        }
        return null;
      })
    );
  }

  /**
   * Consulta directa por ID de usuario
   */
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }
}
