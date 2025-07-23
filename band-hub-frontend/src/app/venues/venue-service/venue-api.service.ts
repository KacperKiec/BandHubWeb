import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {Venue, VenueRequest, VenueUpdateRequest} from '../../core/models/venue.model';

@Injectable({
  providedIn: 'root'
})
export class VenueApiService {

  private apiURL = 'http://localhost:9000/venue';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserVenues(userId: number): Observable<Venue[]> {
    return this.http.get<Venue[]>(`${this.apiURL}/user/${userId}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  addVenue(venue: VenueRequest): Observable<Venue> {
    return this.http.post<Venue>(`${this.apiURL}`, venue, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  deleteVenue(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateVenue(venue: VenueUpdateRequest): Observable<Venue> {
    return this.http.put<Venue>(`${this.apiURL}`, venue, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
