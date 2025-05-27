import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BandInputComponent } from './band-input.component';

describe('BandInputComponent', () => {
  let component: BandInputComponent;
  let fixture: ComponentFixture<BandInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BandInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BandInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
