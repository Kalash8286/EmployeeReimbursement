import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from '../model/users';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  baseURI = "http://localhost:8080/api/reimbursements/";

  constructor(private http: HttpClient) { }


  authenticateUser(user: Users): Observable<Users> {

    return this.http.post<Users>(this.baseURI + "users", user);
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('username')
    let role = sessionStorage.getItem('role');
    return (user !== null && role !== null);
  }

  isUserEmployee() {
    let role = sessionStorage.getItem('role');
    return (role === 'employee');
  }

  isUserTravelExcecutive() {
    let role = sessionStorage.getItem('role');
    return (role === 'travel executive');
  }

  isUserHr() {
    let role = sessionStorage.getItem('role');
    return (role === 'hr');
  }

  logoutUser() {
    sessionStorage.clear();
  }

}
