import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserBandsComponent } from './user-bands.component';

describe('UserBandsComponent', () => {
  let component: UserBandsComponent;
  let fixture: ComponentFixture<UserBandsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserBandsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserBandsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
