import {Component, ElementRef, EventEmitter, HostListener, OnInit, Output, ViewChild} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../core/auth/service/auth.service';
import {AuthApiService} from '../../core/auth/auth-service/auth-api.service';
import {NotificationService} from '../../notifications/notification-socket-service/notification.service';
import {Subscription} from 'rxjs';
import {NotificationPanelComponent} from '../../notifications/notification-panel/notification-panel.component';
import {NotificationApiService} from '../../notifications/notification-service/notification-api.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    RouterLink,
    NotificationPanelComponent
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  userId!: number;
  isUserLoggedIn = false;
  showDropdown = false;
  showNotificationPanel = false;

  private subscription: Subscription = new Subscription();
  notifications: boolean[] = [];

  @ViewChild('dropdownMenu') dropdownRef!: ElementRef;
  @ViewChild('notificationPanel', { read: ElementRef, static: false }) panelRef!: ElementRef;

  @Output() emitOpeningMenu: EventEmitter<boolean> = new EventEmitter();

  constructor(private router: Router, private authService: AuthService, private authApiService: AuthApiService, private eRef: ElementRef, private notificationService: NotificationService, private notificationApiService: NotificationApiService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.authService.isLoggedIn$.subscribe(status => {
      this.isUserLoggedIn = status;

      if(status) {
        this.notificationService.connect();
      }
    });

    this.subscription = this.notificationService.notifications$.subscribe(
      (notification) => {
        if (notification) this.notifications.push(notification);
      }
    );
    this.initNotifications();
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    const clickedInsideDropdown = this.dropdownRef?.nativeElement.contains(event.target);
    const clickedToggle = this.eRef.nativeElement.querySelector('.log-button')?.contains(event.target);
    const clickedInsidePanel = this.panelRef?.nativeElement?.contains(event.target) ?? false;

    if (!clickedInsideDropdown && !clickedToggle) {
      this.showDropdown = false;
    }

    if (!clickedInsidePanel) {
      this.showNotificationPanel = false;
    }
  }

  openMenu() {
    this.emitOpeningMenu.emit(true);
  }

  toggleNotificationPanel(event: MouseEvent) {
    event.stopPropagation();
    this.showNotificationPanel = !this.showNotificationPanel;
    this.showDropdown = false;
  }

  initNotifications() {
    if (!this.userId) return;

    this.notifications = [];
    this.notificationApiService.getUnreadUserNotifications(this.userId).subscribe(notifications => {
      if (notifications) {
        for (let notification of notifications) {
          this.notifications.push(true);
        }
      }
    });
  }

  updateNotifications() {
    this.notifications.pop();
  }

  action() {
    if(!this.isUserLoggedIn) {
      this.router.navigate(['auth/login']);
    } else {
      this.showDropdown = !this.showDropdown;
      this.showNotificationPanel = false;
    }
  }

  goToUserProfile() {
    this.router.navigate(['/profile']);
  }

  goToUserBands() {
    this.router.navigate(['/user/bands']);
  }

  goToUserVenues() {
    this.router.navigate(['/user/renting']);
  }

  logOut() {
    this.authApiService.logout().subscribe(() => {
      this.authService.setLoginStatus(false);
      this.notificationService.disconnect();
      this.showDropdown = false;
      this.router.navigate(['/']);
    });
  }

}
