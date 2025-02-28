package io.kk.bandhub.eventservice.dto;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public EventDTO toEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setDate(event.getDate());
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setEndTime(event.getEndTime());
        eventDTO.setTicketPrice(event.getTicketPrice().doubleValue());
        eventDTO.setOrganizerId(event.getOrganizerId());
        eventDTO.setVenueId(event.getVenueId());
        return eventDTO;
    }
}
