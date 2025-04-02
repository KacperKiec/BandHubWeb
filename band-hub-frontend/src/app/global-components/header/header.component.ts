import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
    imports: [
        NgOptimizedImage,
        NgIf,
        RouterLink,
        RouterOutlet
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Input() isUserLoggedIn = false;
  @Output() emitOpeningMenu: EventEmitter<boolean> = new EventEmitter();

  openMenu() {
    this.emitOpeningMenu.emit(true);
  }
}
