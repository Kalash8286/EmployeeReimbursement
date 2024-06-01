import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Users } from '../model/users';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  showError = "";

  constructor(private loginService: LoginService, private router: Router) { }

  login(formData: Users) {

    this.showError = "";
    this.loginService.authenticateUser(formData)
      .subscribe((data) => {
        if (data.username == formData.username && data.password == formData.password) {
          sessionStorage.setItem('username', data.username);
          sessionStorage.setItem('role', data.role);
          sessionStorage.setItem('loggedInJust', 'true');
          if (data.role === "employee") {
            this.router.navigate(['add-reimbursement']);
          }
          else {
            this.router.navigate(['reimbursements-list']);
          }

        }
      },
        (error) => this.showError = error.error
      )

  }
}
