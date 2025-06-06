import {Component, OnInit} from '@angular/core';
import {NotificationApiService} from '../../services/notification-service/notification-api.service';
import {NgForOf, NgIf} from '@angular/common';
import {NotificationDTO} from '../../core/models/notification.model';

@Component({
  selector: 'app-notification-panel',
  standalone: true,
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './notification-panel.component.html',
  styleUrl: './notification-panel.component.css'
})
export class NotificationPanelComponent implements OnInit {

  userId!: number;
  notifications!: NotificationDTO[];

  constructor(private notificationApiService: NotificationApiService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.notificationApiService.getUserNotifications(this.userId).subscribe(notifications => {
      if (notifications) {
        this.notifications = notifications;
      }
    })
  }
}
