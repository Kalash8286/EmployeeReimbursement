import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { LoginService } from '../login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardHRService implements CanActivate {

  
  constructor(private service: LoginService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.service.isUserLoggedIn() && this.service.isUserHr()) {
      return true;
    }
    else {

      alert("Not Authorized!")
      return false;
    }

  }

}
