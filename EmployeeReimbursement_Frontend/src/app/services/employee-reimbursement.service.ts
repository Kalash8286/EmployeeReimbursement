import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { Reimbursement } from '../model/reimbursement';
import { ReimbursementType } from '../model/reimbursement-type';
import { NewReimbursementRequest } from '../model/new-reimbursement';

@Injectable({
  providedIn: 'root'
})
export class EmployeeReimbursementService {

  baseURI = "http://localhost:8080/api/reimbursements/";


  constructor(private http: HttpClient) { }


  getReimbursementById(requestId: string): Observable<Reimbursement> {

    return this.http.get<Reimbursement>(this.baseURI + requestId);
  }

  getReimbursementTypes(): Observable<ReimbursementType[]> {

    return this.http.get<ReimbursementType[]>(this.baseURI + "types");
  }

  getReimbursementForTravelRequestId(travelRequestId: string): Observable<Reimbursement[]> {

    return this.http.get<Reimbursement[]>(this.baseURI + travelRequestId + "/requests");
  }

  processReimbursementRequest(reimbursementRequest: Reimbursement): Observable<Reimbursement> {

    return this.http.put<Reimbursement>(this.baseURI + reimbursementRequest.id + "/process",
      reimbursementRequest);
  }

  addReimbursementRequest(newRequest: NewReimbursementRequest, file: File): Observable<NewReimbursementRequest> {

    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('requestDTO', JSON.stringify(newRequest));

    return this.http.post<NewReimbursementRequest>(this.baseURI + "add", formData);
    
  }


}
