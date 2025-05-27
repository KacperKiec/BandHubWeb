import {Component, ElementRef, EventEmitter, HostListener, OnInit, Output, ViewChild} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../core/auth/service/auth.service';
import {AuthApiService} from '../../services/auth-service/auth-api.service';

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
export class HeaderComponent implements OnInit{
  isUserLoggedIn = false;
  showDropdown = false;

  @ViewChild('dropdownMenu') dropdownRef!: ElementRef;

  @Output() emitOpeningMenu: EventEmitter<boolean> = new EventEmitter();

  constructor(private router: Router, private authService: AuthService, private authApiService: AuthApiService, private eRef: ElementRef) {
  }

  ngOnInit() {
    this.authService.isLoggedIn$.subscribe(status => {
      this.isUserLoggedIn = status;
    });
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    const clickedInsideDropdown = this.dropdownRef?.nativeElement.contains(event.target);
    const clickedToggle = this.eRef.nativeElement.querySelector('.log-button')?.contains(event.target);

    if (!clickedInsideDropdown && !clickedToggle) {
      this.showDropdown = false;
    }
  }

  openMenu() {
    this.emitOpeningMenu.emit(true);
  }

  action() {
    if(!this.isUserLoggedIn) {
      this.router.navigate(['auth/login']);
    } else {
      this.showDropdown = !this.showDropdown;
    }
  }

  goToUserProfile() {
    this.router.navigate(['/profile']);
  }

  goToUserBands() {
    this.router.navigate(['/user/bands']);
  }

  logOut() {
    this.authApiService.logout().subscribe(() => {
      this.authService.setLoginStatus(false);
      this.showDropdown = false;
      this.router.navigate(['/']);
    });
  }

}
