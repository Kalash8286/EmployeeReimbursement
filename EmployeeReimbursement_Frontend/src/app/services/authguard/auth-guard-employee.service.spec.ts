import { TestBed } from '@angular/core/testing';

import { AuthGuardEmployeeService } from './auth-guard-employee.service';

describe('AuthGuardEmployeeService', () => {
  let service: AuthGuardEmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthGuardEmployeeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
