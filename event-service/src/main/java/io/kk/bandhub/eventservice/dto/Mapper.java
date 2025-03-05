package io.kk.bandhub.eventservice.dto;

import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.service.AccountServiceClient;
import io.kk.bandhub.eventservice.service.MusicServiceClient;
import io.kk.bandhub.eventservice.service.VenueServiceClient;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final VenueServiceClient venueServiceClient;
    private final AccountServiceClient accountServiceClient;
    private final MusicServiceClient musicServiceClient;

    public Mapper(VenueServiceClient venueServiceClient, AccountServiceClient accountServiceClient, MusicServiceClient musicServiceClient) {
        this.venueServiceClient = venueServiceClient;
        this.accountServiceClient = accountServiceClient;
        this.musicServiceClient = musicServiceClient;
    }

    public EventDTO toEventDTO(Event event) {
        EventDTO eventDTO = setEventFields(event);
        eventDTO.setBandDTO(musicServiceClient.getBand(event.getBandId()));
        eventDTO.setVenueId(event.getVenueId());
        eventDTO.setVenueDTO(venueServiceClient.getVenue(event.getVenueId()));
        return eventDTO;
    }

    public EventDTO toEventDTOReg(Event event) {
        EventDTO eventDTO = setEventFields(event);
        eventDTO.setBandDTO(musicServiceClient.getBandReg(event.getBandId()));
        eventDTO.setVenueId(event.getVenueId());
        eventDTO.setVenueDTO(venueServiceClient.getVenue(event.getVenueId()));
        return eventDTO;
    }

    private EventDTO setEventFields(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setDate(event.getDate());
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setEndTime(event.getEndTime());
        eventDTO.setTicketPrice(event.getTicketPrice().doubleValue());
        eventDTO.setOrganizerId(event.getOrganizerId());
        eventDTO.setOrganizerDTO(accountServiceClient.getAccount(event.getOrganizerId()));
        eventDTO.setBandId(event.getBandId());
        return eventDTO;
    }
}
