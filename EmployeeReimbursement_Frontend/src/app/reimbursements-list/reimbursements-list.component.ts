import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Reimbursement } from '../model/reimbursement';
import { EmployeeReimbursementService } from '../services/employee-reimbursement.service';

@Component({
  selector: 'app-reimbursements-list',
  templateUrl: './reimbursements-list.component.html',
  styleUrls: ['./reimbursements-list.component.css']
})
export class ReimbursementsListComponent implements OnInit {

  showError: string = "";
  reimbursementsList: Reimbursement[] = [];
  travelRequestId: any;

  constructor(private route: ActivatedRoute, private service: EmployeeReimbursementService) { }

  ngOnInit(): void {
    if (sessionStorage.getItem('loggedInJust') == 'true') {
      window.location.reload();
      sessionStorage.setItem('loggedInJust', 'false');
    }
    if (this.route.snapshot.paramMap.has('id')) {
      this.travelRequestId = this.route.snapshot.paramMap.get("id");
      this.fetchReimbursementsList(this.travelRequestId);
    }

  }

  fetchReimbursementsList(travelRequestId: any) {
    this.showError = "";
    this.reimbursementsList = [];
    this.service.getReimbursementForTravelRequestId(travelRequestId).subscribe(
      (result) => {
        this.reimbursementsList = result
      },
      (error) => {
        this.showError = error.error;
      }
    );

  }


}
