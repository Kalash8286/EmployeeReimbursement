import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { NewReimbursementComponent } from './new-reimbursement/new-reimbursement.component';
import { ProcessReimbursementComponent } from './process-reimbursement/process-reimbursement.component';
import { ReimbursementsListComponent } from './reimbursements-list/reimbursements-list.component';
import { AuthGuardEmployeeService } from './services/authguard/auth-guard-employee.service';
import { AuthGuardLoginService } from './services/authguard/auth-guard-login.service';
import { AuthGuardTravelExeService } from './services/authguard/auth-guard-travel-exe.service';

const routes: Routes = [
  { path: "", redirectTo: "login", pathMatch: "full" },
  { path: "login", component: LoginComponent},
  { path: "logout", component: LogoutComponent , canActivate:[AuthGuardLoginService]},
  { path: "add-reimbursement", component: NewReimbursementComponent, canActivate: [AuthGuardEmployeeService] },
  { path: "reimbursements-list", component: ReimbursementsListComponent, canActivate:[AuthGuardLoginService] },
  { path: "process-reimbursement/:id", component: ProcessReimbursementComponent, canActivate: [AuthGuardTravelExeService] }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
