import {Page} from './pagination.model';

export interface MessageDTO {
  id: string;
  chatId: string;
  senderId: number;
  receiverId: number;
  content: string;
  timestamp: Date;
  isReceived: boolean;
  isRead: boolean;
}

export interface ChatDTO {
  id: string;
  messages: Page<MessageDTO>;
  receiverFirstName: string;
  receiverLastName: string;
  receiverId: number;
}
