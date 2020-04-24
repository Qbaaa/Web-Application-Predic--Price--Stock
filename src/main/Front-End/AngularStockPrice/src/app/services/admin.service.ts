import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserResponse } from "../response/user-response";


const URL = 'http://localhost:8080/admin';
const httpOptions = {headers: new HttpHeaders({ 'Content-Type': 'application/json' })};

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getUser(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(URL , httpOptions);
  }

  deleteUser(email): Observable<any> {
    return this.http.delete(URL + '/' + email, httpOptions);
  }

}
