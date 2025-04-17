import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {SearchDisplayItem} from '../../core/models/types';

@Component({
  selector: 'app-search-result',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './search-result.component.html',
  styleUrl: './search-result.component.css'
})
export class SearchResultComponent {
  @Input() results: SearchDisplayItem[] = [];
  @Input() currentPage = 0;
  @Input() totalPages = 0;

  @Output() pageChange = new EventEmitter<number>();
  @Output() showDetails = new EventEmitter<number>();

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.pageChange.emit(page);
    }
  }

  goToDetails(id: number) {
    this.showDetails.emit(id);
  }
}
