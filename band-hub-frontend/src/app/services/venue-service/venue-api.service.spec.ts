import { TestBed } from '@angular/core/testing';

import { VenueApiService } from './venue-api.service';

describe('VenueApiService', () => {
  let service: VenueApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VenueApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
