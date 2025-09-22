import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {Page} from '../../core/models/pagination.model';
import {Band, BandMembershipRequest, BandRequest, BandUpdateRequest} from '../../core/models/band.model';

@Injectable({
  providedIn: 'root'
})
export class BandApiService {

  private apiURL = 'http://localhost:9000/band'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getBands(page: number = 0, size: number = 10): Observable<Page<Band>> {
    return this.http.get<Page<Band>>(`${this.apiURL}/public/all?page=${page}&size=${size}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getBandById(id: number): Observable<Band> {
    return this.http.get<Band>(`${this.apiURL}/public/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getBandsWithParams(filters: {name: string, value: string[]}[], page: number = 0, size: number = 10): Observable<Page<Band>> {
    if (filters.length === 0) {
      return this.getBands(page, size);
    }

    let params = new HttpParams();

    for(let filter of filters) {
      for (let val of filter.value) {
        params = params.append(filter.name, val);
      }
    }

    return this.http.get<Page<Band>>(`${this.apiURL}/public/search?page=${page}&size=${size}`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  addBand(band: BandRequest): Observable<Band> {
    return this.http.post<Band>(`${this.apiURL}`, band, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateBand(band: BandUpdateRequest): Observable<Band> {
    return this.http.put<Band>(`${this.apiURL}`, band, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  leaveBand(request: BandMembershipRequest): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/leave`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  deleteBand(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateImage(url: string, bandId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/${bandId}/image`, { url }, { withCredentials: true })
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
