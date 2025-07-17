import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NotificationApiService} from '../../services/notification-service/notification-api.service';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {Notification} from '../../core/models/notification.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-notification-panel',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    DatePipe,
    NgClass
  ],
  templateUrl: './notification-panel.component.html',
  styleUrl: './notification-panel.component.css'
})
export class NotificationPanelComponent implements OnInit {

  userId!: number;
  notifications: Notification[] = [];
  expandedNotificationId: number | null = null;
  showUnread: boolean = true;

  emojiMap: Record<string, string> = {
    BAND_INVITATION: '✉️',
    BAND_REQUEST: '📝',
    JOINED_BAND: '✅',
    BAND_REJECTED_REQUEST: '❌',
    NEW_MESSAGE: '💬',
    NEW_EVENT: '📅',
    NEW_BOOKING: '📌'
  };

  currentPage = 0;
  pageSize = 4;
  totalPages = 0;

  @Output() markNotification: EventEmitter<boolean> = new EventEmitter<boolean>();


  constructor(private notificationApiService: NotificationApiService, private router: Router) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.loadUnreadNotifications(0);
  }

  loadAllNotifications(page: number) {
    this.notificationApiService.getUserNotifications(this.userId, page, this.pageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
        this.notifications.push(...response.content);
      },
      error: (err) => console.error('Error occurred:', err)
    });
  }

  loadUnreadNotifications(page: number) {
    this.notificationApiService.getPagedUnreadUserNotifications(this.userId, page, this.pageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
        this.notifications.push(...response.content);
      },
      error: (err) => console.error('Error occurred:', err)
    });
  }

  loadMore() {
    const nextPage = this.currentPage + 1;
    if(this.showUnread) {
      this.loadUnreadNotifications(nextPage);
    }
    else this.loadAllNotifications(nextPage);
  }

  changeToAllNotifications() {
    this.notifications = [];
    this.showUnread = false;
    this.loadAllNotifications(0);
  }

  changeToUnreadNotifications() {
    this.notifications = [];
    this.showUnread = true;
    this.loadUnreadNotifications(0);
  }

  getEmoji(type: string): string {
    return this.emojiMap[type] || '';
  }

  onSummaryClick(id: number) {
    this.toggleExpanded(id);
    this.markAsRead(id);
  }

  toggleExpanded(id: number) {
    this.expandedNotificationId = this.expandedNotificationId === id ? null : id;
  }

  markAsRead(id: number) {
    const toMark = this.notifications.find(notification => notification.id === id);

    if(toMark && !toMark.read) {
      toMark.read = true;
      this.notificationApiService.markNotificationAsRead(id).subscribe();
      this.markNotification.emit(true);
    }
  }

  action(notification: Notification) {
    switch (notification.type.toString()) {
      case "BAND_INVITATION": {
        this.router.navigate(['invitations'], { queryParams: { mode: 'invitations' } });
        break;
      }
      case "BAND_REQUEST": {
        this.router.navigate(['invitations'], { queryParams: { mode: 'requests' } });
        break;
      }
      case "JOINED_BAND": {
        const bandId = notification.metadata['bandId'];
        this.router.navigate(['/bands', bandId]);
        break;
      }
      case "BAND_REJECTED_REQUEST": {
        const bandId = notification.metadata['bandId'];
        this.router.navigate(['/bands', bandId]);
        break;
      }
      case "NEW_MESSAGE": {

        break;
      }
      case "NEW_EVENT": {

        break;
      }
      case "NEW_BOOKING": {

        break;
      }
      default:
    }
  }
}
