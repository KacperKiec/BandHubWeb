import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';
import {catchError, Observable} from 'rxjs';
import {NotificationDTO} from '../../core/models/notification.model';
import {Page} from '../../core/models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationApiService {
  private apiURL = 'http://localhost:9000/notification';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) {}

  getUserNotifications(userId: number, page: number = 0, size: number = 6): Observable<Page<NotificationDTO>> {
    return this.http.get<Page<NotificationDTO>>(`${this.apiURL}/${userId}?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getPagedUnreadUserNotifications(userId: number, page: number = 0, size: number = 6): Observable<Page<NotificationDTO>> {
    return this.http.get<Page<NotificationDTO>>(`${this.apiURL}/unread/paged/${userId}?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getUnreadUserNotifications(userId: number): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.apiURL}/unread/${userId}`, {withCredentials: true})
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  markNotificationAsRead(notificationId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/${notificationId}`, {},  {withCredentials: true})
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
