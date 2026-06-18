import { TestBed } from '@angular/core/testing';

import { BikeCatalogService } from './bike-catalog.service';

describe('BikeCatalogService', () => {
  let service: BikeCatalogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BikeCatalogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
