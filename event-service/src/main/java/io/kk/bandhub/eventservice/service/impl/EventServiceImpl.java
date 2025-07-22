package io.kk.bandhub.eventservice.service.impl;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.dto.EventRequestDTO;
import io.kk.bandhub.eventservice.dto.EventUpdateRequestDTO;
import io.kk.bandhub.eventservice.dto.Mapper;
import io.kk.bandhub.eventservice.exception.EventError;
import io.kk.bandhub.eventservice.exception.EventException;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.repository.EventRepository;
import io.kk.bandhub.eventservice.repository.EventSpecification;
import io.kk.bandhub.eventservice.service.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final Mapper mapper;

    @Transactional
    public void createEvent(EventRequestDTO eventDTO) {

        Event event = new Event();
        event.setBandId(eventDTO.getBandId());
        event.setDate(eventDTO.getDate());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        event.setTicketPrice(BigDecimal.valueOf(eventDTO.getTicketPrice()));
        event.setDescription(eventDTO.getDescription());
        event.setBookingId(eventDTO.getBookingId());

        mapper.toEventDTOReg(event);
        eventRepository.save(event);
    }

    @Transactional
    public EventDTO updateEvent(EventUpdateRequestDTO eventDTO) {
        var existingEvent = eventRepository.findById(eventDTO.getId()).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );

        existingEvent.setVenueId(eventDTO.getBookingId());
        existingEvent.setBandId(eventDTO.getBandId());
        existingEvent.setDate(eventDTO.getDate());
        existingEvent.setStartTime(eventDTO.getStartTime());
        existingEvent.setEndTime(eventDTO.getEndTime());
        existingEvent.setDescription(eventDTO.getDescription());
        existingEvent.setTicketPrice(BigDecimal.valueOf(eventDTO.getTicketPrice()));
        existingEvent.setBookingId(eventDTO.getBookingId());

        var mappedEvent = mapper.toEventDTOReg(existingEvent);
        eventRepository.save(existingEvent);
        return mappedEvent;
    }

    public void deleteEvent(Integer eventId) {
        eventRepository.findById(eventId).orElseThrow(
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

    public List<EventDTO> searchEvents(LocalDate date, Integer bandId) {

        Specification<Event> spec = EventSpecification.hasDate(date)
                .and(EventSpecification.hasBandId(bandId));

        var events = eventRepository.findAll(spec);
        return events.stream().map(mapper::toEventDTO).toList();
    }
}
