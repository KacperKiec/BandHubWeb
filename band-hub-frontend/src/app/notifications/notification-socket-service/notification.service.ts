import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private stompClient: any;
  private apiUrl = environment.apiUrl;
  private notificationSubject = new BehaviorSubject<any>(null);
  public notifications$ = this.notificationSubject.asObservable();
  private isConnected = false;

  public connect(): void {
    if (this.isConnected) return;

    const socket = new SockJS(`${this.apiUrl}/ws/notifications`);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.isConnected = true;

      const userId = localStorage.getItem('userId');
      if (userId) {
        this.stompClient.subscribe(`/topic/notification/${userId}`, (message: any) => {
          this.notificationSubject.next(JSON.parse(message.body));
        });
      }
    }, (error: any) => {
      this.isConnected = false;
    });
  }

  public disconnect(): void {
    if (this.stompClient && this.isConnected) {
      this.stompClient.disconnect();
      this.isConnected = false;
    }
  }
}
