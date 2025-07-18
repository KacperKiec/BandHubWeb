import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarouselSelectorComponent } from './carousel-selector.component';

describe('CarouselSelectorComponent', () => {
  let component: CarouselSelectorComponent;
  let fixture: ComponentFixture<CarouselSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarouselSelectorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarouselSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
