import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ChatDTO, MessageDTO, MessageRequest} from '../../core/models/chat.model';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ChatApiService} from '../chat-service/chat-api.service';
import {UserApiService} from '../../user/user-service/user-api.service';
import {AccountResponse} from '../../core/models/account.model';
import {Page, PageInfo} from '../../core/models/pagination.model';
import {ChatService} from '../chat-socket-service/chat.service';

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
  messages: MessageDTO[] = [];

  selectedChat: ChatDTO | null = null;
  messageCurrentPage: number = 0;
  messageTotalPages: number = 0;
  messagePageSize: number = 10;
  hasChats: boolean = false;

  users: AccountResponse[] = [];
  userCurrentPage: number = 0;
  userTotalPages: number = 0;
  userPageSize: number = 5;

  searchText = '';
  messageContent = '';
  isLoadingOlder = false;

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;


  constructor(private router: Router, private route: ActivatedRoute, private chatApiService: ChatApiService, private userApiService: UserApiService, private chatSocketService: ChatService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.route.queryParams.subscribe(params => {
      if (params['receiverFirstName'] && params['receiverLastName']) {
        this.searchText = params['receiverFirstName'].concat(' ').concat(params['receiverLastName']);
        this.filterUsers();
      }
    });

    this.chatApiService.getUserChats(this.userId, this.messageCurrentPage, this.messagePageSize).subscribe(chats => {
      if (chats) {
        if (this.chats.length > 0) this.hasChats = true;
        this.chats = [...chats];
      }
    });

    this.chatSocketService.messages$.subscribe(message => {
      this.messages = [...this.messages, message];

      this.scrollToBottom();
    });
  }

  hasUnreadMessages(chat: ChatDTO): boolean {
    return chat.messages.content.some(msg => msg.receiverId === this.userId && !msg.isRead);
  }

  isMessageFromCurrentUser(msg: MessageDTO): boolean {
    return msg.senderId === this.userId;
  }

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }

  selectUser(receiver: AccountResponse) {
    let chat = this.chats.find(chat => chat.receiverId === receiver.id);
    if (chat) {
      this.selectChat(chat);
    } else {
      this.startChat(receiver);
      this.searchText = '';
    }
  }

  selectChat(chat: ChatDTO) {
    this.selectedChat = chat
    this.messageTotalPages = chat.messages.page.totalPages;
    this.messageCurrentPage = chat.messages.page.number;
    this.messagePageSize = chat.messages.page.size;
    this.messages = [...chat.messages.content];

    this.markMessages();

    setTimeout(() => this.scrollToBottom(), 0);
  }

  startChat(receiver: AccountResponse) {
    this.chatApiService.createChat([this.userId, receiver.id]).subscribe(id => {
      if (id) {

        const pageInfo: PageInfo = {
          totalPages: 0,
          totalElements: 0,
          size: 0,
          number: 0
        }

        const page: Page<MessageDTO> = {
          content: [],
          page: pageInfo
        }

        const dto: ChatDTO = {
          id: id,
          messages: page,
          receiverFirstName: receiver.firstName,
          receiverLastName: receiver.lastName,
          receiverId: receiver.id
        }

        this.chats.push(dto);
        this.selectChat(dto);
      }
    });
  }

  filterUsers() {
    this.userCurrentPage = 0;
    this.loadUsersFromApi();
  }

  trackByMsg(index: number, msg: MessageDTO) {
    return msg.id || (msg.timestamp ? new Date(msg.timestamp).getTime() : index);
  }

  loadMoreMessages(event: Event) {
    const el = event.target as HTMLElement;

    const reachedTop = el.scrollTop <= 0;

    const noMorePages = this.messageCurrentPage >= this.messageTotalPages - 1;

    if (!reachedTop || this.isLoadingOlder || noMorePages || !this.selectedChat) return;

    this.isLoadingOlder = true;

    const distanceFromBottom = el.scrollHeight - el.scrollTop;

    const prevBehavior = el.style.scrollBehavior;
    el.style.scrollBehavior = 'auto';

    const nextPage = this.messageCurrentPage + 1;

    this.chatApiService
      .getChatMessages(this.selectedChat.id, nextPage, this.messagePageSize)
      .subscribe({
        next: (response) => {
          if (!response) { this.isLoadingOlder = false; el.style.scrollBehavior = prevBehavior; return; }

          this.messageTotalPages = response.page.totalPages;
          this.messageCurrentPage = response.page.number;
          this.messagePageSize = response.page.size;

          const older = [...response.content];
          this.messages = [...older, ...this.messages];

          this.markMessages();

          requestAnimationFrame(() => {
            requestAnimationFrame(() => {
              el.scrollTop = el.scrollHeight - distanceFromBottom;
              el.style.scrollBehavior = prevBehavior;
              this.isLoadingOlder = false;
            });
          });
        },
        error: () => {
          this.isLoadingOlder = false;
          el.style.scrollBehavior = prevBehavior;
        }
      });
  }

  markMessages() {
    const temp = this.messages.filter(msg => msg.receiverId === this.userId && !msg.isRead).map(msg => msg.id);

    if (temp.length === 0) return;

    this.chatApiService.markMessagesAsRead(temp).subscribe({
      next: () => {
        this.messages.forEach(msg => msg.isRead = true);
      },
      error: () => {
        this.messages.forEach(msg => msg.isRead = false);
      }
    });
  }

  loadMoreUsers(event: Event) {
    const target = event.target as HTMLElement;

    const reachedTop =
      Math.ceil(target.scrollTop + target.clientHeight) >= target.scrollHeight;

    if (reachedTop) {
      this.userCurrentPage++;

      this.loadUsersFromApi();
    }
  }

  private loadUsersFromApi() {
    let temp = this.searchText.split(' ');
    if (temp.length > 1) {

      this.userApiService.getUsersByName(temp[0], temp[1], this.userCurrentPage, this.userPageSize).subscribe(response => {
        if (response) {
          this.users = [...response.content];
          this.userTotalPages = response.page.totalPages;
          this.userCurrentPage = response.page.number;
          this.userPageSize = response.page.size;
        }
      });

    } else {

      this.userApiService.getUsersByName(temp[0], temp[0], this.userCurrentPage, this.userPageSize).subscribe(response => {
        if (response) {
          this.users = [...response.content];
          this.userTotalPages = response.page.totalPages;
          this.userCurrentPage = response.page.number;
          this.userPageSize = response.page.size;
        }
      });

    }
  }

  sendMessage() {
    if (this.messageContent && this.selectedChat) {
      const message: MessageRequest = {
        chatId: this.selectedChat?.id,
        senderId: this.userId,
        receiverId: this.selectedChat?.receiverId,
        content: this.messageContent,
        timestamp: new Date(),
        isReceived: false,
        isRead: false
      }

      this.chatSocketService.sendMessage(message);
      this.messageContent = '';

      this.messages.push({
        id: '0',
        chatId: message.chatId,
        senderId: message.senderId,
        receiverId: message.receiverId,
        content: message.content,
        timestamp: message.timestamp,
        isReceived: false,
        isRead: false
      });

      setTimeout(() => this.scrollToBottom(), 0);

    } else return
  }

  private scrollToBottom(): void {
    try {
      setTimeout(() => {
        if (this.messagesContainer) {
          this.messagesContainer.nativeElement.scrollTop =
            this.messagesContainer.nativeElement.scrollHeight;
        }
      }, 100);
    } catch (err) {
      console.error('Scroll error:', err);
    }
  }
}
