import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditPasswordFormComponent } from './edit-password-form.component';

describe('EditPasswordFormComponent', () => {
  let component: EditPasswordFormComponent;
  let fixture: ComponentFixture<EditPasswordFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditPasswordFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditPasswordFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
