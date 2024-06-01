import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeReimbursementService } from '../services/employee-reimbursement.service';
import { Reimbursement } from '../model/reimbursement';

@Component({
  selector: 'app-process-reimbursement',
  templateUrl: './process-reimbursement.component.html',
  styleUrls: ['./process-reimbursement.component.css']
})
export class ProcessReimbursementComponent implements OnInit {

  reimbursement: Reimbursement = new Reimbursement();
  selectedStatus: string ="";
  remark: string ="";
  requestId: any;
  showSuccess: boolean = false;
  showError: string = "";
  
  constructor(private route: ActivatedRoute, private service: EmployeeReimbursementService, private router: Router) { }
  ngOnInit(): void {
    this.requestId = this.route.snapshot.paramMap.get("id");
    this.service.getReimbursementById(this.requestId).subscribe(data => {
      this.reimbursement= data;
    });   
  }

  
  goBack(){
    this.router.navigate(["/reimbursements-list", {id: this.reimbursement.travelRequestId}]);
  }
  
  updateRequest() {
    this.showSuccess =false;
    this.showError="";
    this.reimbursement.status = this.selectedStatus;
    this.reimbursement.remark = this.remark;
    
    this.service.processReimbursementRequest(this.reimbursement).subscribe(
      (result) =>{
        if(result.id == this.requestId)
        {
          this.showSuccess=true;
        }
      }, 
      (error) => {
        this.showError = error.error;
      }
    );



  }




}
