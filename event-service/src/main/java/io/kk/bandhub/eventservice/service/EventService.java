package io.kk.bandhub.eventservice.service;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.dto.Mapper;
import io.kk.bandhub.eventservice.exception.EventError;
import io.kk.bandhub.eventservice.exception.EventException;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.repository.EventRepository;
import io.kk.bandhub.eventservice.repository.EventSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final Mapper mapper;

    public EventService(EventRepository eventRepository, Mapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void createEvent(EventDTO eventDTO) {
        var existingEvent = eventRepository.findByVenueIdAndOrganizerIdAndDateAndStartTimeAndName(eventDTO.getVenueId(), eventDTO.getOrganizerId(), eventDTO.getDate(), eventDTO.getStartTime(), eventDTO.getName());
        if (existingEvent.isPresent()) {
            throw new EventException(EventError.EVENT_ALREADY_EXISTS);
        }

        Event event = new Event();

        event.setVenueId(eventDTO.getVenueId());
        event.setOrganizerId(eventDTO.getOrganizerId());
        event.setDate(eventDTO.getDate());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        event.setTicketPrice(BigDecimal.valueOf(eventDTO.getTicketPrice()));
        event.setDescription(eventDTO.getDescription());
        event.setName(eventDTO.getName());
        event.setBandId(eventDTO.getBandId());

        var mappedEvent = mapper.toEventDTO(event);
        eventRepository.save(event);
    }

    @Transactional
    public EventDTO updateEvent(EventDTO eventDTO) {
        var existingEvent = eventRepository.findById(eventDTO.getId()).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );

        existingEvent.setVenueId(eventDTO.getVenueId());
        existingEvent.setOrganizerId(eventDTO.getOrganizerId());
        existingEvent.setDate(eventDTO.getDate());
        existingEvent.setStartTime(eventDTO.getStartTime());
        existingEvent.setEndTime(eventDTO.getEndTime());
        existingEvent.setName(eventDTO.getName());
        existingEvent.setDescription(eventDTO.getDescription());
        existingEvent.setTicketPrice(BigDecimal.valueOf(eventDTO.getTicketPrice()));
        existingEvent.setBandId(eventDTO.getBandId());

        var mappedEvent = mapper.toEventDTO(existingEvent);
        mappedEvent.setOrganizerDTO(eventDTO.getOrganizerDTO());
        eventRepository.save(existingEvent);
        return mappedEvent;
    }

    public void deleteEvent(Integer eventId) {
        var existingEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );
        eventRepository.deleteById(eventId);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(mapper::toEventDTO).toList();
    }

    public EventDTO getEventById(Integer eventId) {
        return mapper.toEventDTO(eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        ));
    }

    public Page<EventDTO> searchEvents(
        Pageable pageable,
        List<String> cities,
        String name,
        LocalDate date,
        Integer organizerId) {

        Specification<Event> spec = EventSpecification.hasCities(cities)
                .and(EventSpecification.hasDate(date))
                .and(EventSpecification.hasName(name))
                .and(EventSpecification.hasOrganizerId(organizerId));

        var events = eventRepository.findAll(spec, pageable);
        return events.map(mapper::toEventDTO);
    }
}
