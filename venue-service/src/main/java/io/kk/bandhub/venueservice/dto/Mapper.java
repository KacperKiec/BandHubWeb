package io.kk.bandhub.venueservice.dto;

import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.Venue;
import io.kk.bandhub.venueservice.service.AccountServiceClient;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final AccountServiceClient accountServiceClient;

    public Mapper(AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;
    }

    public VenueDTO toVenueDTOReg(Venue venue) {
        AccountDTO accountDTO = accountServiceClient.getAccountInRegistration(venue.getOwnerId());

        return setVenueDTOFields(venue, accountDTO);
    }


    public VenueDTO toVenueDTO(Venue venue) {
        AccountDTO accountDTO = accountServiceClient.getAccount(venue.getOwnerId());

        return setVenueDTOFields(venue, accountDTO);
    }

    private VenueDTO setVenueDTOFields(Venue venue, AccountDTO accountDTO) {
        VenueDTO venueDTO = new VenueDTO();
        venueDTO.setId(venue.getId());
        venueDTO.setName(venue.getName());
        venueDTO.setDescription(venue.getDescription());
        venueDTO.setAddress(venue.getAddress());
        venueDTO.setCity(venue.getCity());
        venueDTO.setCapacity(venue.getCapacity());
        venueDTO.setOwnerDTO(accountDTO);
        venueDTO.setOwnerId(accountDTO.getId());
        return venueDTO;
    }

    public OfferDTO toOfferDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setDate(offer.getDate());
        offerDTO.setStartTime(offer.getStartTime());
        offerDTO.setEndTime(offer.getEndTime());
        offerDTO.setVenueId(offer.getVenue().getId());
        offerDTO.setVenueName(offer.getVenue().getName());
        offerDTO.setPricePerHour(offer.getPricePerHour().doubleValue());
        offerDTO.setStatus(offer.getStatus());
        offerDTO.setPurpose(offer.getPurpose());
        return offerDTO;
    }

    public BookingDTO toBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setOfferId(booking.getOffer().getId());
        bookingDTO.setVenueName(booking.getOffer().getVenue().getName());
        bookingDTO.setTenantId(booking.getTenantId());
        var accountDTO = accountServiceClient.getAccount(bookingDTO.getTenantId());
        bookingDTO.setTenantFirstName(accountDTO.getFirstName());
        bookingDTO.setTenantLastName(accountDTO.getLastName());
        return bookingDTO;
    }
}
