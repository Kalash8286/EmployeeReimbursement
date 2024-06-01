import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { LoginService } from '../login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardLoginService {

  constructor(private service: LoginService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.service.isUserLoggedIn()) {
      return true;
    }
    else {
      alert("Please Login!")
      this.router.navigate(['login']);
      return false;
    }

  }

}
