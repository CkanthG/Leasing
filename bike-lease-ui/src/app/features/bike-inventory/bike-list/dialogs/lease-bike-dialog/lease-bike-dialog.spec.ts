import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaseBikeDialog } from './lease-bike-dialog';

describe('LeaseBikeDialog', () => {
  let component: LeaseBikeDialog;
  let fixture: ComponentFixture<LeaseBikeDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaseBikeDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeaseBikeDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
