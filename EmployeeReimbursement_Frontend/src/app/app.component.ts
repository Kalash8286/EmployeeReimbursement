import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Employee Reimbursement';

  username = sessionStorage.getItem('username');
  role = sessionStorage.getItem('role');
}
