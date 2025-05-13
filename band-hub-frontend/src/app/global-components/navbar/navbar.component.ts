import {Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import {NgClass, NgIf, NgOptimizedImage} from '@angular/common';
import {RouterLink, RouterOutlet} from '@angular/router';
import {AuthService} from '../../core/auth/service/auth.service';

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
export class NavbarComponent implements OnInit{
  @Input() isMenuOpen = false;
  @Input() isUserLoggedIn = false;
  @Output() closeMenuEmitter = new EventEmitter<boolean>();

  private count: number = 0;

  constructor(private el: ElementRef, private authService: AuthService) {}

  ngOnInit() {
    this.authService.isLoggedIn$.subscribe(status => {
      this.isUserLoggedIn = status;
    });
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
