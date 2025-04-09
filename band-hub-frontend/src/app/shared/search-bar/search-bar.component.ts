import {Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, SimpleChanges} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {filterElement} from '../../core/models/types';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgForOf,
    FormsModule,
    NgIf
  ],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})
export class SearchBarComponent implements OnInit{
  public dropdownStatus = false;
  private isCheckboxClicked: boolean[] = [];

  @Input() filters: {label: string, type: string, options?: string[]}[] = [];
  @Output() emitParams: EventEmitter<filterElement[]> = new EventEmitter();
  currentFilters: filterElement[]= [];
  filterValue: string[] = [];
  rangeValues: {min: number, max: number}[] = [];
  searchFieldContent = '';

  @Input() placeholderText = '';

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    this.filterValue = new Array<string>(this.filters.length);
    this.rangeValues = this.filters.map(() => ({ min: 0, max: 99 }));
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['filters'] && this.filters) {
      this.filters.sort((a, b) => b.type.localeCompare(a.type));
    }
  }

  toggleDropdown() {
    this.dropdownStatus = !this.dropdownStatus;
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    if(!this.el.nativeElement.contains(event.target)) {
      this.dropdownStatus = false;
    }
  }

  addParam(index: number){
    const inputValue = this.filterValue[index];

    if(this.filters[index].type === 'checkbox'){
      const isLookingFor = this.currentFilters.find(filter => filter.name === this.filters[index].label);

      if(isLookingFor){
        if(inputValue) {
          isLookingFor.value = 'tak';
          this.isCheckboxClicked[index] = true;
        } else {
          isLookingFor.value = 'nie';
          this.isCheckboxClicked[index] = false;
        }
        return;
      } else {
        this.isCheckboxClicked[index] = !!inputValue;
        this.currentFilters.push({name: this.filters[index].label, value: this.isCheckboxClicked[index] ? 'tak' : 'nie'});
      }
      return;
    }
    else if(this.filters[index].type === 'range'){
      const isExisting = this.currentFilters.find(filter => filter.name === this.filters[index].label);

      if(isExisting){
        isExisting.value = `${this.rangeValues[index].min} - ${this.rangeValues[index].max}`
      } else {
        this.currentFilters.push({name: this.filters[index].label, value: `${this.rangeValues[index].min} - ${this.rangeValues[index].max}`});
      }
      return;
    }

    if (inputValue) {
      if (this.isParamOnList({name: this.filters[index].label, value: inputValue})) return;

      const addedFilter = this.currentFilters.find(filter => filter.name === this.filters[index].label);
      if (addedFilter && addedFilter.value !== inputValue) {
        this.currentFilters.push({name: this.filters[index].label, value: inputValue});
      } else {
        this.currentFilters.push({name: this.filters[index].label, value: inputValue});
      }
    }
  }

  private isParamOnList(data: filterElement): boolean {
    if(this.currentFilters.length === 0) return false;

    let valueExists = false;

    this.currentFilters.forEach(filter => {
      if(filter.name.toLowerCase().includes(data.name.toLowerCase())) {
        if(filter.value === data.value) valueExists = true;
      }
    });
    return valueExists;
  }

  removeParam(index: number): void {
    this.currentFilters.splice(index, 1);
  }

  updateRangeValues(index: number): void {
    if (this.rangeValues[index].min > this.rangeValues[index].max) {
      this.rangeValues[index].min === 0 ? this.rangeValues[index].min = 0 : this.rangeValues[index].min = this.rangeValues[index].max - 1;
    }
  }

  confirmSearching(): void {
    const filtersToEmit: filterElement[] = [];

    if(this.searchFieldContent) {
      const existingName = this.currentFilters.find(filter => filter.name === `${this.placeholderText}`);
      if(existingName){
        existingName.value = this.searchFieldContent;
      } else this.currentFilters.push({name: `${this.placeholderText}`,value: this.searchFieldContent });
    }

    for (let index in this.currentFilters) {
      if (this.currentFilters[index].value === 'nie') {
        filtersToEmit.push({ name: this.currentFilters[index].name, value: 'false' });
        continue;
      } else if (this.currentFilters[index].value === 'tak') {
        filtersToEmit.push({ name: this.currentFilters[index].name, value: 'true' });
        continue;
      } else if (this.currentFilters[index].name === 'name') continue;
      filtersToEmit.push(this.currentFilters[index]);
    }

    console.log(filtersToEmit)
    this.emitParams.emit(filtersToEmit);
  }
}
