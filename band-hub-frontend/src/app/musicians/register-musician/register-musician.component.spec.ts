import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterMusicianComponent } from './register-musician.component';

describe('RegisterMusicianComponent', () => {
  let component: RegisterMusicianComponent;
  let fixture: ComponentFixture<RegisterMusicianComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterMusicianComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterMusicianComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
