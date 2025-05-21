import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonalInfoInputComponent } from './personal-info-input.component';

describe('PersonalInfoInputComponent', () => {
  let component: PersonalInfoInputComponent;
  let fixture: ComponentFixture<PersonalInfoInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PersonalInfoInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PersonalInfoInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
