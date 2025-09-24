import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {
  EventDTO,
  EventRequestDTO, EventSearchingParams,
  EventUpdateRequestDTO,
  FollowRequest,
  UserEventDTO
} from '../../core/models/event.model';
import {Page} from '../../core/models/pagination.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventApiService {

  private apiURL = environment.apiUrl + '/event'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserEvents(id: number): Observable<UserEventDTO[]> {
    return this.http.get<UserEventDTO[]>(`${this.apiURL}/user/${id}`, { withCredentials: true })
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getEvent(id: number): Observable<EventDTO> {
    return this.http.get<EventDTO>(`${this.apiURL}/public/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      )
  }

  createEvent(request: EventRequestDTO): Observable<UserEventDTO> {
    return this.http.post<UserEventDTO>(`${this.apiURL}`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateEvent(request: EventUpdateRequestDTO): Observable<UserEventDTO> {
    return this.http.put<UserEventDTO>(`${this.apiURL}`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getEvents(eventParams: EventSearchingParams, page: number = 0, size: number = 10): Observable<Page<EventDTO>> {
    let params = new HttpParams();

    if (eventParams.cities && eventParams.cities.length > 0) {
      eventParams.cities.forEach(city => {
        params = params.append('cities', city.toString());
      });
    }

    if (eventParams.date) {
      params = params.set('date', eventParams.date);
    }

    if (eventParams.bandName) {
      params = params.set('bandName', eventParams.bandName);
    }

    return this.http.get<Page<EventDTO>>(`${this.apiURL}/public/search?page=${page}&size=${size}`, {params})
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

  followEvent(request: FollowRequest): Observable<UserEventDTO> {
    return this.http.post<UserEventDTO>(`${this.apiURL}/follow`, request, {withCredentials: true})
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
