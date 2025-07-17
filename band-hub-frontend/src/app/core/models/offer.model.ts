export interface Offer {
  id: number;
  venueId: number;
  venueName: string;
  pricePerHour: number;
  date: string;
  startTime: string;
  endTime: string;
  status: OfferStatus;
  availableRanges: OfferAvailability[];
}

export enum OfferStatus {
  ACTIVE = "Aktywna",
  CLAIMED = "Zajęta",
  CANCELED = "Anulowana"
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
