import {Component, ElementRef, HostListener, Input, Renderer2} from '@angular/core';
import {NgForOf, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgForOf,
    FormsModule
  ],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})
export class SearchBarComponent {
  public dropdownStatus = false;
  @Input() public parametersNumber: number = 3;

  filters= ["asf", "agasdg", "asgsdg"];
  filterValue: string[] = [];

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit(): void {
    this.filterValue = new Array<string>(this.parametersNumber);
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
    if (inputValue) {
      console.log("nie puste!");
      this.addParamObject(index);
    } else {
      console.log("puste!");
    }
  }

  private addParamObject(index: number){
    const inputValue = this.filterValue[index];
    const paramName: string = this.filters[index];

    const paramElement = this.renderer.createElement('div');
    const paramText = this.renderer.createElement('label');
    this.renderer.setProperty(paramText, 'textContent', `${paramName}: ${inputValue}`);
    this.renderer.appendChild(paramElement, paramText);
    this.renderer.addClass(paramElement, 'param-element');

    const paramBox = document.getElementById('param-box');

    if(paramBox){
      paramBox.appendChild(paramElement);
    }
  }
}
