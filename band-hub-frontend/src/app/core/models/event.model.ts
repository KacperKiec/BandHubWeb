export interface EventDTO {
  id: number;
  name: string;
  bandId: number;
  bandName: string;
  venueId: number;
  venueName: string;
  bookingId: number;
  street: string;
  number: string;
  city: string;
  postalCode: string;
  country: string;
  date: string;
  startTime: string;
  endTime: string;
  description: string;
  ticketPrice: number;
  followersCount: number;
  creatorId: number;
}

export interface EventRequestDTO {
  creatorId: number;
  name: string;
  bandId: number;
  bookingId: number;
  venueId: number;
  date: string;
  startTime: string;
  endTime: string;
  description: string;
  ticketPrice: number;
}

export interface EventUpdateRequestDTO {
  id: number;
  creatorId: number;
  name: string;
  bandId: number;
  bookingId: number;
  venueId: number;
  date: string;
  startTime: string;
  endTime: string;
  description: string;
  ticketPrice: number;
}

export interface FollowRequest {
  userId: number;
  eventId: number;
}

export interface UserEventDTO {
  id: number;
  userId: number;
  bookingId: number;
  eventId: number;
  eventName: string;
  date: string;
  startTime: string;
  endTime: string;
  relation: RelationType;
}

export enum RelationType {
  LANDLORD = "LANDLORD",
  PARTICIPANT = "PARTICIPANT",
  FOLLOWER = "FOLLOWER"
}

export interface EventSearchingParams {
  cities: String[],
  date: string | null,
  bandName :string
}
