import {Component, OnInit} from '@angular/core';
import {ChatDTO, MessageDTO} from '../../core/models/chat.model';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {ChatApiService} from '../chat-service/chat-api.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    DatePipe
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit {
  userId!: number;
  chats: ChatDTO[] = [];

  currentChat: ChatDTO | null = null;
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;
  hasChats: boolean = false;


  currentUserId = 1;  // załóżmy, że to ID zalogowanego użytkownika

  mockChats = [
    { id: 2, userName: 'Anna Kowalska' },
    { id: 3, userName: 'Piotr Nowak' },
    { id: 4, userName: 'Marta Wiśniewska' }
  ];

  users = ['Anna Kowalska', 'Piotr Nowak', 'Marta Wiśniewska', 'Jan Kowal', 'Katarzyna Zielińska'];

  selectedChatId = '2'; // ID aktualnie wybranego rozmówcy

  messages: MessageDTO[] = [
    {
      id: 'm1',
      chatId: '1',
      senderId: 2,
      receiverId: 1,
      content: 'Cześć, jak się masz?',
      timestamp: new Date('2025-08-12T10:00:00'),
      isReceived: true,
      isRead: true
    },
    {
      id: 'm2',
      chatId: '2',
      senderId: 1,
      receiverId: 2,
      content: 'Wszystko dobrze, dzięki! A u Ciebie?',
      timestamp: new Date('2025-08-12T10:02:00'),
      isReceived: false,
      isRead: true
    },
    {
      id: 'm3',
      chatId: '3',
      senderId: 2,
      receiverId: 1,
      content: 'Też dobrze, dzięki za pytanie!',
      timestamp: new Date('2025-08-12T10:05:00'),
      isReceived: true,
      isRead: false
    }
  ];

  searchText = '';

  constructor(private router: Router, private chatApiService: ChatApiService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.chatApiService.getUserChats(this.userId).subscribe(chats => {
      if (chats) {
        if (this.chats.length > 0) this.hasChats = true;
        this.chats = [...chats];
      }
    });
  }

  get filteredUsers() {
    return this.users.filter(u => u.toLowerCase().includes(this.searchText.toLowerCase()));
  }

  selectChat(chat: ChatDTO) {
    this.currentChat = chat
    this.totalPages = chat.messages.page.totalPages;
    this.currentPage = chat.messages.page.number;
    this.pageSize = chat.messages.page.size;
    this.messages = [...chat.messages.content];
  }

  isMessageFromCurrentUser(msg: MessageDTO): boolean {
    return msg.senderId === this.currentUserId;
  }

  findChat() {
    // return this.mockChats.find(c => c.id === this.selectedChatId)?.userName || 'Wybierz czat'
  }

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }
}
