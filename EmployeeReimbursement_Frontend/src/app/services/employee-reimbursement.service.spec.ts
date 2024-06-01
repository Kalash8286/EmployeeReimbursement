import { TestBed } from '@angular/core/testing';

import { EmployeeReimbursementService } from './employee-reimbursement.service';

describe('EmployeeReimbursementService', () => {
  let service: EmployeeReimbursementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeReimbursementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
