import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {Offer, OfferRequest, OfferUpdateRequest} from '../../core/models/offer.model';

@Injectable({
  providedIn: 'root'
})
export class OfferApiService {

  private apiURL = 'http://localhost:9000/offer';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserOffers(userId: number): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.apiURL}/user/${userId}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  addOffer(offer: OfferRequest): Observable<Offer> {
    return this.http.post<Offer>(`${this.apiURL}`, offer)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  deleteOffer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateOffer(offer: OfferUpdateRequest): Observable<Offer> {
    return this.http.put<Offer>(`${this.apiURL}`, offer)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
