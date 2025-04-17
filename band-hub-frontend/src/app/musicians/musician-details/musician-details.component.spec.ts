import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MusicianDetailsComponent } from './musician-details.component';

describe('MusicianDetailsComponent', () => {
  let component: MusicianDetailsComponent;
  let fixture: ComponentFixture<MusicianDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MusicianDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MusicianDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
