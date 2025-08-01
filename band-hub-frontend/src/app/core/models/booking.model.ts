export interface Booking {
  id: number;
  offerId: number;
  venueId: number;
  venueName: string;
  tenantId: number;
  tenantFirstName: string;
  tenantLastName: string;
  startTime: string;
  endTime: string;
  bookingDate: string;
  bookedDate: string;
  pricePerHour: number;
}

export interface BookingRequest {
  tenantId: number;
  offerId: number;
  startTime: string;
  endTime: string;
  bookingDate: string;
}
