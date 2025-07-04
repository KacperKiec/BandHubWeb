import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RentingPanelComponent } from './renting-panel.component';

describe('RentingPanelComponent', () => {
  let component: RentingPanelComponent;
  let fixture: ComponentFixture<RentingPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RentingPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RentingPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
