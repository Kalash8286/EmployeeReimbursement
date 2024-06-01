import { TestBed } from '@angular/core/testing';

import { AuthGuardTravelExeService } from './auth-guard-travel-exe.service';

describe('AuthGuardTravelExeService', () => {
  let service: AuthGuardTravelExeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthGuardTravelExeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
