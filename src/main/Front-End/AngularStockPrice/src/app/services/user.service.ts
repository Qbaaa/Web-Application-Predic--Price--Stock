import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

const URL = 'http://localhost:8080/user';
const httpOptions = {headers: new HttpHeaders({ 'Content-Type': 'application/json' })};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  deleteUser(email): Observable<any> {
    return this.http.delete(URL + '/' + email, httpOptions);
  }

}
