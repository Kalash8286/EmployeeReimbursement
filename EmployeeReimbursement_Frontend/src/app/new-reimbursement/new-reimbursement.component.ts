import { Component, OnInit } from '@angular/core';
import { EmployeeReimbursementService } from '../services/employee-reimbursement.service';
import { NewReimbursementRequest } from '../model/new-reimbursement';
import { ReimbursementType } from '../model/reimbursement-type';
import { first } from 'rxjs';

@Component({
  selector: 'app-new-reimbursement',
  templateUrl: './new-reimbursement.component.html',
  styleUrls: ['./new-reimbursement.component.css']
})
export class NewReimbursementComponent implements OnInit {

  newRequest = new NewReimbursementRequest();
  reimbursementTypeId: number = 101;
  file!: File;
  showSuccess = false;
  showError: string = "";

  constructor(private service: EmployeeReimbursementService) { }
  ngOnInit(): void {
    if (sessionStorage.getItem('loggedInJust') == 'true') {
      window.location.reload();
      sessionStorage.setItem('loggedInJust', 'false');
    }
  }


  onChange(event: any) {
    this.file = event.target.files[0];
  }

  getReimbursementType(typeId: number) {
    this.service.getReimbursementTypes().subscribe((data) => {

      this.newRequest.reimbursementTypeDTO = data.find((type) => type.id == typeId)!;
      
    });
  }

  saveRequest() {
    this.service.addReimbursementRequest(this.newRequest, this.file).pipe(first()).subscribe(
      (data) => {
        this.showSuccess = true;
      },
      (error) => {
        this.showError = error.error;
      }
    );
  }

  onSubmit() {
    this.showError = "";
    this.showSuccess = false;

    if (Object.values(this.newRequest).length < 9 || this.file === undefined) {
      this.showError = "Enter valid values!"
    }
    else if (new Date(this.newRequest.requestProcessedOn) < (new Date())) {
      this.showError = "Request Processed On Date must be a Future Date";
    }
    else if (new Date(this.newRequest.invoiceDate) > (new Date())) {
      this.showError = "Invoice Date must be a Past Date";
    } else {
      this.getReimbursementType(this.reimbursementTypeId);
      this.saveRequest();
    }

  }





}
