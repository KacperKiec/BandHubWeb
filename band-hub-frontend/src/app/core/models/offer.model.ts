export interface Offer {
  id: number;
  venueId: number;
  venueName: string;
  capacity: number;
  pricePerHour: number;
  date: string;
  startTime: string;
  endTime: string;
  status: OfferStatus;
  availableRanges: OfferAvailability[];
  latitude: number;
  longitude: number;
}

export enum OfferStatus {
  ACTIVE = "ACTIVE",
  CLAIMED = "CLAIMED",
  CANCELED = "CANCELED"
}

export interface OfferAvailability {
  startTime: string;
  endTime: string;
}

export interface OfferRequest {
  venueId: number;
  pricePerHour: number;
  date: string;
  startTime: string;
  endTime: string;
  status: OfferStatus;
}

export interface OfferUpdateRequest {
  id: number;
  venueId: number;
  pricePerHour: number;
  date: string;
  startTime: string;
  endTime: string;
  status: OfferStatus;
}

export interface OfferSearchingParams {
  cities: String[],
  status: OfferStatus,
  date: string,
  minLatitude: number,
  maxLatitude: number,
  minLongitude: number,
  maxLongitude: number
}
