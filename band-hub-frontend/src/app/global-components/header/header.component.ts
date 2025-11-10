import {Component, ElementRef, EventEmitter, HostListener, OnInit, Output, ViewChild} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../auth/service/auth.service';
import {AuthApiService} from '../../auth/auth-service/auth-api.service';
import {NotificationService} from '../../notifications/notification-socket-service/notification.service';
import {Subscription} from 'rxjs';
import {NotificationPanelComponent} from '../../notifications/notification-panel/notification-panel.component';
import {NotificationApiService} from '../../notifications/notification-service/notification-api.service';
import {ChatService} from '../../chat/chat-socket-service/chat.service';
import {ChatApiService} from '../../chat/chat-service/chat-api.service';

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

  private notificationSubscription: Subscription = new Subscription();
  private chatSubscription: Subscription = new Subscription();
  notifications: boolean[] = [];
  messages: boolean[] = [];

  @ViewChild('dropdownMenu') dropdownRef!: ElementRef;
  @ViewChild('notificationPanel', { read: ElementRef, static: false }) panelRef!: ElementRef;

  @Output() emitOpeningMenu: EventEmitter<boolean> = new EventEmitter();

  constructor(private router: Router, private authService: AuthService, private authApiService: AuthApiService, private eRef: ElementRef, private notificationService: NotificationService, private notificationApiService: NotificationApiService, private chatSocketService: ChatService, private chatApiService: ChatApiService) {
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
        this.chatSocketService.connect();
      }
    });

    this.notificationSubscription = this.notificationService.notifications$.subscribe(
      (notification) => {
        if (notification) this.notifications = [...this.notifications, notification];
      }
    );
    this.initNotifications();

    this.chatSubscription = this.chatSocketService.messages$.subscribe(
      (message) => {
        if (message) this.messages = [...this.messages, message];
      }
    );
    this.chatSocketService.readStatusChanged$.subscribe(() => {
      this.initMessages();
    });
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
      if (notifications && notifications.length > 0) {
        this.notifications = notifications.map(() => true);
      }
    });
  }

  initMessages() {
    if (!this.userId) return;

    this.messages = [];
    this.chatApiService.checkUnreadMessages(this.userId).subscribe(messages => {
      if (messages && messages.length > 0) {
        this.messages = messages.map(() => true);
      }
    })
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

  goToRentingPanel() {
    this.router.navigate(['/user/renting']);
  }

  goToUserBookings() {
    this.router.navigate(['/user/bookings']);
  }

  goToUserEvents() {
    this.router.navigate(['/user/events']);
  }

  goToChat() {
    this.router.navigate(['/chat']);
  }

  logOut() {
    this.authApiService.logout().subscribe(() => {
      this.authService.setLoginStatus(false);
      this.notificationService.disconnect();
      this.chatSocketService.disconnect();
      this.showDropdown = false;
      this.router.navigate(['/']);
    });
  }

}
