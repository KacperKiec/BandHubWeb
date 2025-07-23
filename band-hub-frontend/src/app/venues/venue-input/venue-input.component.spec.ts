import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueInputComponent } from './venue-input.component';

describe('VenueInputComponent', () => {
  let component: VenueInputComponent;
  let fixture: ComponentFixture<VenueInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VenueInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VenueInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
