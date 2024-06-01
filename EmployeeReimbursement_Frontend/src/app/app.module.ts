import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReimbursementsListComponent } from './reimbursements-list/reimbursements-list.component';
import { ProcessReimbursementComponent } from './process-reimbursement/process-reimbursement.component';
import { NewReimbursementComponent } from './new-reimbursement/new-reimbursement.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';

@NgModule({
  declarations: [
    AppComponent,
    ReimbursementsListComponent,
    ProcessReimbursementComponent,
    NewReimbursementComponent,
    LoginComponent,
    LogoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
