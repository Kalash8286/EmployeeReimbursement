import { TestBed } from '@angular/core/testing';

import { AuthGuardHRService } from './auth-guard-hr.service';

describe('AuthGuardHRService', () => {
  let service: AuthGuardHRService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthGuardHRService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
