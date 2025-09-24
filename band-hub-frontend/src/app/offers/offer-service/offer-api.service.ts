import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {Offer, OfferRequest, OfferSearchingParams, OfferUpdateRequest} from '../../core/models/offer.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OfferApiService {

  private apiURL = environment.apiUrl + '/offer';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getUserOffers(userId: number): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.apiURL}/user/${userId}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  addOffer(offer: OfferRequest): Observable<Offer> {
    return this.http.post<Offer>(`${this.apiURL}`, offer, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  deleteOffer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateOffer(offer: OfferUpdateRequest): Observable<Offer> {
    return this.http.put<Offer>(`${this.apiURL}`, offer, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getOffers(offerParams: OfferSearchingParams): Observable<Offer[]> {
    let params = new HttpParams();

    if (offerParams.status) {
      params = params.set('status', offerParams.status);
    }

    if (offerParams.date) {
      params = params.set('date', offerParams.date);
    }

    if (offerParams.minLatitude !== null && offerParams.minLatitude !== undefined) {
      params = params.set('minLatitude', offerParams.minLatitude.toString());
    }

    if (offerParams.maxLatitude !== null && offerParams.maxLatitude !== undefined) {
      params = params.set('maxLatitude', offerParams.maxLatitude.toString());
    }

    if (offerParams.minLongitude !== null && offerParams.minLongitude !== undefined) {
      params = params.set('minLongitude', offerParams.minLongitude.toString());
    }

    if (offerParams.maxLongitude !== null && offerParams.maxLongitude !== undefined) {
      params = params.set('maxLongitude', offerParams.maxLongitude.toString());
    }

    if (offerParams.cities && offerParams.cities.length > 0) {
      offerParams.cities.forEach(city => {
        params = params.append('cities', city.toString());
      });
    }

    return this.http.get<Offer[]>(`${this.apiURL}/public/search`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
