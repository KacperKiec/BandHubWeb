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
        var venueEventData = venueServiceClient.getVenueById(event.getVenueId());
        var bandEventData = musicServiceClient.getBand(event.getBandId());

        return buildDTO(event, venueEventData, bandEventData);
    }

    public EventDTO toEventDTOReg(Event event, Integer creatorId) {
        var venueEventData = venueServiceClient.getVenueEventData(event.getBookingId());
        var bandData = musicServiceClient.getBandReg(event.getBandId());

        if (event.getStartTime().isBefore(venueEventData.getStartTime())
                || event.getEndTime().isAfter(venueEventData.getEndTime())) {
            throw new RegistrationException("Event time out of booking bounds");
        }

        if (!creatorId.equals(venueEventData.getTenantId())) {
            throw new RegistrationException("Creator is not the booking tenant");
        }

        if (!creatorId.equals(bandData.getFounderId())) {
            throw new RegistrationException("Creator must be the band founder");
        }

        VenueDTO venue = VenueDTO.builder()
                .id(venueEventData.getVenueId())
                .name(venueEventData.getVenueName())
                .street(venueEventData.getStreet())
                .number(venueEventData.getNumber())
                .city(venueEventData.getCity())
                .postalCode(venueEventData.getPostalCode())
                .country(venueEventData.getCountry())
                .build();

        return buildDTO(event, venue, bandData);
    }


    public EventDTO toEventDTOOwnerReg(Event event, Integer creatorId) {
        var venueData = venueServiceClient.getVenueById(event.getVenueId());
        var bandData = musicServiceClient.getBandReg(event.getBandId());

        if (!creatorId.equals(venueData.getOwnerId())) {
            throw new RegistrationException("Only venue owner can create this event");
        }

        if (!creatorId.equals(bandData.getFounderId())) {
            throw new RegistrationException("Creator must be the band founder");
        }

        return buildDTO(event, venueData, bandData);
    }

    private EventDTO buildDTO(Event event, VenueDTO venue, BandEventDataDTO band) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setVenueId(venue.getId());
        dto.setVenueName(venue.getName());
        dto.setStreet(venue.getStreet());
        dto.setNumber(venue.getNumber());
        dto.setCity(venue.getCity());
        dto.setPostalCode(venue.getPostalCode());
        dto.setCountry(venue.getCountry());

        dto.setBandId(band.getId());
        dto.setBandName(band.getName());

        dto.setDate(event.getDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setDescription(event.getDescription());
        dto.setTicketPrice(event.getTicketPrice().doubleValue());

        return dto;
    }
}
