package io.kk.bandhub.eventservice.dto;

import io.kk.bandhub.eventservice.exception.RegistrationException;
import io.kk.bandhub.eventservice.integration.MusicServiceClient;
import io.kk.bandhub.eventservice.integration.VenueServiceClient;
import io.kk.bandhub.eventservice.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final VenueServiceClient venueServiceClient;
    private final MusicServiceClient musicServiceClient;

    public EventDTO toEventDTO(Event event) {
        EventDTO eventDTO = setBasicEventFields(event);
        eventDTO.setVenueId(event.getVenueId());

        var bookingEventData = venueServiceClient.getBookingEventData(event.getBookingId());

        setVenueEventFields(eventDTO, bookingEventData);

        var bandEventData = musicServiceClient.getBand(event.getBandId());
        eventDTO.setBandId(bandEventData.getId());
        eventDTO.setBandName(bandEventData.getName());
        return eventDTO;
    }

    public EventDTO toEventDTOReg(Event event) {
        EventDTO eventDTO = setBasicEventFields(event);

        var bookingEventData = venueServiceClient.getBookingEventDataReg(event.getBookingId());

        if (event.getStartTime().isBefore(bookingEventData.getStartTime()) || event.getEndTime().isAfter(bookingEventData.getEndTime()))
            throw new RegistrationException("Registration failed because of bad data (event time ranges do not match with booking time ranges)");

        setVenueEventFields(eventDTO, bookingEventData);

        var bandEventData = musicServiceClient.getBandReg(event.getBandId());
        eventDTO.setBandId(bandEventData.getId());
        eventDTO.setBandName(bandEventData.getName());

        if (!bandEventData.getFounderId().equals(bookingEventData.getTenantId())) {
            throw new RegistrationException("Registration failed because of bad data (user does not exist)");
        }

        return eventDTO;
    }

    private EventDTO setBasicEventFields(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setDate(event.getDate());
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setEndTime(event.getEndTime());
        eventDTO.setTicketPrice(event.getTicketPrice().doubleValue());
        return eventDTO;
    }

    private void setVenueEventFields(EventDTO eventDTO, BookingEventDataDTO bookingEventData) {
        eventDTO.setVenueId(bookingEventData.getVenueId());
        eventDTO.setVenueName(bookingEventData.getVenueName());
        eventDTO.setStreet(bookingEventData.getStreet());
        eventDTO.setNumber(bookingEventData.getNumber());
        eventDTO.setCity(bookingEventData.getCity());
        eventDTO.setPostalCode(bookingEventData.getPostalCode());
        eventDTO.setCountry(bookingEventData.getCountry());
    }
}
