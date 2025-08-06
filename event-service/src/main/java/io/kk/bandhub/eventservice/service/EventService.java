package io.kk.bandhub.eventservice.service;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.dto.EventRequestDTO;
import io.kk.bandhub.eventservice.dto.EventUpdateRequestDTO;
import io.kk.bandhub.eventservice.dto.UserEventDTO;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    UserEventDTO createEvent(EventRequestDTO eventDTO);
    UserEventDTO updateEvent(EventUpdateRequestDTO eventDTO);
    void deleteEvent(Integer eventId);
    List<EventDTO> getAllEvents();
    EventDTO getEventById(Integer eventId);
    List<EventDTO> searchEvents(LocalDate date, Integer organizerId, List<String> cities);
}
