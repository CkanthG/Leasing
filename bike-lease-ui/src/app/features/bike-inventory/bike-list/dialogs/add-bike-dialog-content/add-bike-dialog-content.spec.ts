import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBikeDialogContent } from './add-bike-dialog-content';

describe('AddBikeDialogContent', () => {
  let component: AddBikeDialogContent;
  let fixture: ComponentFixture<AddBikeDialogContent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddBikeDialogContent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddBikeDialogContent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
