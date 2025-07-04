import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditVenueFormComponent } from './edit-venue-form.component';

describe('EditVenueFormComponent', () => {
  let component: EditVenueFormComponent;
  let fixture: ComponentFixture<EditVenueFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditVenueFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditVenueFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
