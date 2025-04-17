import { TestBed } from '@angular/core/testing';

import { MusicianApiService } from './musician-api.service';

describe('MusicianApiService', () => {
  let service: MusicianApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusicianApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
