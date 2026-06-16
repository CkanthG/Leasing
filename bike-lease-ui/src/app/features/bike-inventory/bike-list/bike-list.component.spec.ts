import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BikeList } from './bike-list';

describe('BikeList', () => {
  let component: BikeList;
  let fixture: ComponentFixture<BikeList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BikeList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BikeList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
