package io.kk.bandhub.eventservice.dto;

import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.model.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    public UserEventDTO buildUserEventDTO(UserEvent userEvent) {
        return UserEventDTO.builder()
                .id(userEvent.getId())
                .userId(userEvent.getUserId())
                .eventId(userEvent.getEvent().getId())
                .eventName(userEvent.getEvent().getName())
                .date(userEvent.getEvent().getDate())
                .startTime(userEvent.getEvent().getStartTime())
                .endTime(userEvent.getEvent().getEndTime())
                .relation(userEvent.getRelation())
                .build();
    }

    public EventDTO buildEventDTO(Event event, VenueDTO venue, BandEventDataDTO band, Integer followersCount) {
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
        dto.setOwnerId(venue.getOwnerId());
        dto.setBookingId(event.getBookingId());

        dto.setBandId(band.getId());
        dto.setBandName(band.getName());

        dto.setDate(event.getDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setDescription(event.getDescription());
        dto.setTicketPrice(event.getTicketPrice().doubleValue());
        dto.setFollowersCount(followersCount);

        return dto;
    }
}
