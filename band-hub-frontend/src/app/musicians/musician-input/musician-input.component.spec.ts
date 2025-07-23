import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MusicianInputComponent } from './musician-input.component';

describe('MusicianInputComponent', () => {
  let component: MusicianInputComponent;
  let fixture: ComponentFixture<MusicianInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MusicianInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MusicianInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
