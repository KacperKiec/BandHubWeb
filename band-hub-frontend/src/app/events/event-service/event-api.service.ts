import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {
  EventDTO,
  EventRequestDTO,
  EventUpdateRequestDTO,
  FollowRequest,
  UserEventDTO
} from '../../core/models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventApiService {

  private apiURL = 'http://localhost:9000/event'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserEvents(id: number): Observable<UserEventDTO[]> {
    return this.http.get<UserEventDTO[]>(`${this.apiURL}/user/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  createEvent(request: EventRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.apiURL}`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateEvent(request: EventUpdateRequestDTO): Observable<EventDTO> {
    return this.http.put<EventDTO>(`${this.apiURL}`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  followEvent(request: FollowRequest): Observable<void> {
    return this.http.post<void>(`${this.apiURL}/follow`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  unfollowEvent(request: FollowRequest): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/unfollow`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
