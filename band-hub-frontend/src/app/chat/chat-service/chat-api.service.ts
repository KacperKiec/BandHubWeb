import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {ChatDTO, MessageDTO} from '../../core/models/chat.model';
import {Page} from '../../core/models/pagination.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatApiService {

  private apiURL = environment.apiUrl + '/chat'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserChats(id: number, page: number, size: number): Observable<ChatDTO[]> {
    return this.http.get<ChatDTO[]>(`${this.apiURL}/user/${id}?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  createChat(participants: number[]): Observable<string> {
    return this.http.post<string>(`${this.apiURL}`, participants, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getChatMessages(chatId: string, page: number, size: number): Observable<Page<MessageDTO>> {
    return this.http.get<Page<MessageDTO>>(`${this.apiURL}/${chatId}/msg?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      )
  }

  markMessagesAsRead(msgIds: string[]): Observable<void> {
    let params = new HttpParams();
    msgIds.forEach(id => params = params.append('msgIds', id));

    return this.http.patch<void>(`${this.apiURL}/read`, null, { params, withCredentials: true })
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  checkUnreadMessages(userId: number): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>(`${this.apiURL}/user/${userId}/unread`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
