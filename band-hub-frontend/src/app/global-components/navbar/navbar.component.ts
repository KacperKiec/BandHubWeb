import {Component, ElementRef, EventEmitter, HostListener, Input, Output, SimpleChanges} from '@angular/core';
import {NgClass, NgIf, NgOptimizedImage} from '@angular/common';
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgClass,
    NgIf,
    RouterLink,
    RouterOutlet,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  @Input() isMenuOpen = false;
  @Input() isUserLoggedIn = false;
  @Output() closeMenuEmitter = new EventEmitter<boolean>();

  private count: number = 0;

  constructor(private el: ElementRef) {}

  setMenuStatus(open: boolean) {
    this.isMenuOpen = open;
    console.log("isMenuOpen: " + this.isMenuOpen);
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
    this.closeMenuEmitter.emit(this.isMenuOpen);
    this.count = 0;
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    if(!this.el.nativeElement.contains(event.target)) {
      this.count++;
      if (this.count === 2) {
        this.isMenuOpen = false;
        this.closeMenuEmitter.emit(this.isMenuOpen);
        this.count = 0;
      }
    }
  }
}
