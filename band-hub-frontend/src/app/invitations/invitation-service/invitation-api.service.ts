import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {BandMembershipRequest} from '../../core/models/band.model';
import {catchError, Observable, tap} from 'rxjs';
import {
  BandInvitation,
  BandInvitationRequest,
  BandJoinRequest,
  BandJoinUpdateRequest
} from '../../core/models/invitation.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InvitationApiService {

  private apiURL = environment.apiUrl + '/band/joining'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  sendRequest(request: BandMembershipRequest): Observable<void> {
    return this.http.post<void>(`${this.apiURL}/request`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  sendInvitation(invitation: BandMembershipRequest): Observable<void> {
    return this.http.post<void>(`${this.apiURL}/invite`, invitation, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  respondRequest(request: BandJoinUpdateRequest): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/request`, request, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  respondInvitation(invitation: BandInvitationRequest): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/invitation`, invitation, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getUserInvitations(): Observable<BandInvitation[]> {
    return this.http.get<BandInvitation[]>(`${this.apiURL}/user/invitations`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getBandRequests(bandId: number): Observable<BandJoinRequest[]> {
    return this.http.get<BandJoinRequest[]>(`${this.apiURL}/${bandId}/requests`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
