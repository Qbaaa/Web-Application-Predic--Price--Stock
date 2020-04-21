import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH = 'http://localhost:8080/';
const httpOptions = {headers: new HttpHeaders({ 'Content-Type': 'application/json' })};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  register(user): Observable<any> {
    return this.http.post(AUTH + 'register', {
      username: user.username,
      email: user.email,
      password: user.password,
      role: user.role
    }, httpOptions);
  }

  login(user): Observable<any> {
    return this.http.post(AUTH + 'login',{
      username: user.username,
      password: user.password
    }, httpOptions);
  }

}
