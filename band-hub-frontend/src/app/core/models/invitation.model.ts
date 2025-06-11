export interface BandInvitation {
  id: number;
  bandId: number;
  bandName: string;
  invitedMusicianId: number;
  status: RequestStatus;
  creationDate: string;
}

export interface BandInvitationRequest {
  id: number;
  bandId: number;
  invitedMusicianId: number;
  status: RequestStatus;
}

export interface BandJoinRequest {
  id: number;
  bandId: number;
  bandName: string;
  applicantId: number;
  applicantFirstName: string;
  applicantLastName: string;
  status: RequestStatus;
  creationDate: string;
}

export interface BandJoinUpdateRequest {
  id: number;
  bandId: number;
  applicantId: number;
  status: RequestStatus;
}

export enum RequestStatus {
  PENDING,
  REJECTED,
  ACCEPTED,
  CANCELED
}
