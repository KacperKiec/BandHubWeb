import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private stompClient: any;
  private notificationSubject = new BehaviorSubject<any>(null);
  public notifications$ = this.notificationSubject.asObservable();
  private isConnected = false;

  public connect(): void {
    if (this.isConnected) return;

    const socket = new SockJS('http://localhost:9000/ws/notifications');
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.isConnected = true;
      //console.log('Connected to WebSocket');

      const userId = localStorage.getItem('userId');
      if (userId) {
        //console.log('Subscribing to /topic/notification/' + userId);
        this.stompClient.subscribe(`/topic/notification/${userId}`, (message: any) => {
          this.notificationSubject.next(JSON.parse(message.body));
        });
      }
    }, (error: any) => {
      //console.error('WebSocket error:', error);
      this.isConnected = false;
    });
  }

  public disconnect(): void {
    if (this.stompClient && this.isConnected) {
      this.stompClient.disconnect();
      this.isConnected = false;
      //console.log('Disconnected from WebSocket');
    }
  }
}
