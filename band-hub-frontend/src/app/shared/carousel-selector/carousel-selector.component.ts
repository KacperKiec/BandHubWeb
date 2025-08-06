import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {CarouselElement} from '../../core/models/shared.model';
import {NgClass, NgForOf, NgStyle} from '@angular/common';

@Component({
  selector: 'app-carousel-selector',
  standalone: true,
  imports: [
    NgForOf,
    NgClass,
    NgStyle
  ],
  templateUrl: './carousel-selector.component.html',
  styleUrl: './carousel-selector.component.css'
})
export class CarouselSelectorComponent implements OnInit, OnChanges {
  @Input() elements: CarouselElement[] = [];
  @Input() visibleCount: number = 3;
  @Input() disabled: boolean = false;
  @Output() emitElement = new EventEmitter<number>();

  currentIndex: number = 0;
  @Input() selectedOptionId: number | null = null;

  constructor(private cdr: ChangeDetectorRef) {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['elements']) {
      this.cdr.detectChanges();
    }
  }

  selectElement(id: number) {
    this.emitElement.emit(id);
    this.selectedOptionId = id;
  }

  goToPrevious() {
    this.currentIndex = (this.currentIndex - 1 + this.elements.length) % this.elements.length;
  }

  goToNext() {
    this.currentIndex = (this.currentIndex + 1) % this.elements.length;
  }

}
