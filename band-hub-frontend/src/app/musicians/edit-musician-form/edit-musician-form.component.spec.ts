import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditMusicianFormComponent } from './edit-musician-form.component';

describe('EditMusicianFormComponent', () => {
  let component: EditMusicianFormComponent;
  let fixture: ComponentFixture<EditMusicianFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditMusicianFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditMusicianFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
