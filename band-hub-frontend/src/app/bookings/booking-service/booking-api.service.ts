import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {BookingRequest} from '../../core/models/booking.model';
import {catchError, Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookingApiService {

  private apiURL = 'http://localhost:9000/booking';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  createBooking(booking: BookingRequest): Observable<void> {
    return this.http.post<void>(`${this.apiURL}`, booking, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  deleteBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getUserBookings(id: number): Observable<BookingRequest[]> {
    return this.http.get<BookingRequest[]>(`${this.apiURL}/user/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getOwnerBookings(id: number): Observable<BookingRequest[]> {
    return this.http.get<BookingRequest[]>(`${this.apiURL}/owner/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
