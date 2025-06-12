import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserVenuesComponent } from './user-venues.component';

describe('UserVenuesComponent', () => {
  let component: UserVenuesComponent;
  let fixture: ComponentFixture<UserVenuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserVenuesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserVenuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
