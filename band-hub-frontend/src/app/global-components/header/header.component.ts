import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
    imports: [
        NgOptimizedImage,
        NgIf,
        RouterLink
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Input() isUserLoggedIn = false;
  @Output() emitOpeningMenu: EventEmitter<boolean> = new EventEmitter();

  constructor(private router: Router) {
  }

  openMenu() {
    this.emitOpeningMenu.emit(true);
  }

  logIn() {
    this.router.navigate(['auth/login']);
  }
}
