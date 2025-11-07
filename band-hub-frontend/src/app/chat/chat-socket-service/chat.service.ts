import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import SockJS from 'sockjs-client';
import {Client, Message} from '@stomp/stompjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private client: Client | null = null;
  private apiUrl = environment.apiUrl;
  private messagesSubject = new BehaviorSubject<any>([]);
  messages$ = this.messagesSubject.asObservable();
  private readStatusChangedSubject = new BehaviorSubject<any>([]);
  readStatusChanged$ = this.readStatusChangedSubject.asObservable();

  constructor() {
  }

  connect() {
    if (this.client && this.client.active) return;

    this.client = new Client({
      brokerURL: '',
      connectHeaders: {},
      reconnectDelay: 5000,
      webSocketFactory: () => new SockJS(`${this.apiUrl}/ws/chat`)
    });

    this.client.onConnect = () => {
      const userId = localStorage.getItem('userId');
      if(userId) {
        this.client!.subscribe(`/topic/user/${userId}`, (message: Message) => {
          const msg = JSON.parse(message.body);
          this.messagesSubject.next(msg);
        });
      }
    };

    this.client.activate();
  }

  disconnect() {
    if (this.client && this.client.active) {
      this.client.deactivate();
    }
  }

  sendMessage(message: any) {
    this.client!.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(message)
    });
  }

  notifyReadStatusChanged() {
    this.readStatusChangedSubject.next(true);
  }
}
