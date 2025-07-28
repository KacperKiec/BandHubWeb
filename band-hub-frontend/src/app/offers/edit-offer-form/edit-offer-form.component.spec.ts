import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditOfferFormComponent } from './edit-offer-form.component';

describe('EditOfferFormComponent', () => {
  let component: EditOfferFormComponent;
  let fixture: ComponentFixture<EditOfferFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditOfferFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditOfferFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
