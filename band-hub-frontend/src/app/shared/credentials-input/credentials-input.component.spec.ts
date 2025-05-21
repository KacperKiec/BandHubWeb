import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CredentialsInputComponent } from './credentials-input.component';

describe('CredentialsInputComponent', () => {
  let component: CredentialsInputComponent;
  let fixture: ComponentFixture<CredentialsInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CredentialsInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CredentialsInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
