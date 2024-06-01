import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessReimbursementComponent } from './process-reimbursement.component';

describe('ProcessReimbursementComponent', () => {
  let component: ProcessReimbursementComponent;
  let fixture: ComponentFixture<ProcessReimbursementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcessReimbursementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcessReimbursementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
