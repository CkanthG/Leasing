import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private readonly apiUrl = `${environment.authApiUrl}/users`;

  constructor(
    private http: HttpClient
  ) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(
      this.apiUrl
    );
  }

  getUser(
    id: number
  ): Observable<User> {
    return this.http.get<User>(
      `${this.apiUrl}/${id}`
    );
  }
}
