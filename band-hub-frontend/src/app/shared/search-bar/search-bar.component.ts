import {Component, ElementRef, HostListener, Input, OnInit, Output} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';

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
  @Output() currentFilters: {name: string, value: string }[]= [];
  filterValue: string[] = [];

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    this.filterValue = new Array<string>(this.filters.length);
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
    console.log(this.currentFilters)
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
        //this.updateCheckboxParamObject(index);
        return;
      } else {
        this.isCheckboxClicked[index] = !!inputValue;
        this.currentFilters.push({name: this.filters[index].label, value: this.isCheckboxClicked[index] ? 'tak' : 'nie'});
      }
      return;
    }

    if (inputValue) {
      if (this.isParamOnList(this.filters[index].label, inputValue)) return;

      const addedFilter = this.currentFilters.find(filter => filter.name === this.filters[index].label);
      if (addedFilter && addedFilter.value !== inputValue) {
        this.currentFilters.push({name: this.filters[index].label, value: inputValue});
      } else {
        this.currentFilters.push({name: this.filters[index].label, value: inputValue});
      }
    }
  }

  private isParamOnList(name: string, value: string): boolean {
    if(this.currentFilters.length === 0) return false;

    let valueExists = false;

    this.currentFilters.forEach(filter => {
      if(filter.name.toLowerCase().includes(name.toLowerCase())) {
        if(filter.value === value) valueExists = true;
      }
    });
    return valueExists;
  }

  public removeParam(index: number): void {
    this.currentFilters.splice(index, 1);
  }
}
