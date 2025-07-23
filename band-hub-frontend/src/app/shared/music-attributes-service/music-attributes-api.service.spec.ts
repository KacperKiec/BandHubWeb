import { TestBed } from '@angular/core/testing';

import { MusicAttributesApiService } from './music-attributes-api.service';

describe('MusicAttributesApiService', () => {
  let service: MusicAttributesApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusicAttributesApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
