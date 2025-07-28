export interface Venue {
  id: number;
  ownerId: number;
  name: string;
  capacity: number;
  description: string;
  street: string;
  number: string;
  city: string;
  postalCode: string;
  country: string;
  latitude: number;
  longitude: number;
}

export interface VenueRequest {
  ownerId: number;
  name: string;
  capacity: number;
  description: string;
  street: string;
  number: string;
  city: string;
  postalCode: string;
  country: string;
  latitude: number;
  longitude: number;
}

export interface VenueUpdateRequest {
  id: number;
  ownerId: number;
  name: string;
  capacity: number;
  description: string;
  street: string;
  number: string;
  city: string;
  postalCode: string;
  country: string;
  latitude: number;
  longitude: number;
}
