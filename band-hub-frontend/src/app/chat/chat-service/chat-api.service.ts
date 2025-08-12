import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {ChatDTO} from '../../core/models/chat.model';

@Injectable({
  providedIn: 'root'
})
export class ChatApiService {

  private apiURL = 'http://localhost:9000/chat'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserChats(id: number, page: number = 0, size: number = 20): Observable<ChatDTO[]> {
    return this.http.get<ChatDTO[]>(`${this.apiURL}/user/${id}?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  createChat(participants: number[]): Observable<void> {
    return this.http.post<void>(`${this.apiURL}`, {participants}, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
