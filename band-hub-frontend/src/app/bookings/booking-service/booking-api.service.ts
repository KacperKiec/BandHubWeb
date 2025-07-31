import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {Booking, BookingRequest} from '../../core/models/booking.model';
import {catchError, Observable, tap} from 'rxjs';
import {Page} from '../../core/models/pagination.model';

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

  deleteBooking(id: number, initiator: string): Observable<void> {
    let params = new HttpParams();
    params = params.append('initiator', initiator);

    return this.http.delete<void>(`${this.apiURL}/${id}`, {params, withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getUserBookings(id: number, page: number = 0, size: number = 10): Observable<Page<Booking>> {
    return this.http.get<Page<Booking>>(`${this.apiURL}/user/${id}?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getOwnerBookings(id: number, page: number = 0, size: number = 10): Observable<Page<Booking>> {
    return this.http.get<Page<Booking>>(`${this.apiURL}/owner/${id}?page=${page}&size=${size}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
