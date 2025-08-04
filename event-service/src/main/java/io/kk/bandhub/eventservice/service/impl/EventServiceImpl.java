package io.kk.bandhub.eventservice.service.impl;

import io.kk.bandhub.eventservice.dto.*;
import io.kk.bandhub.eventservice.exception.EventError;
import io.kk.bandhub.eventservice.exception.EventException;
import io.kk.bandhub.eventservice.exception.RegistrationException;
import io.kk.bandhub.eventservice.integration.VenueServiceClient;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.repository.EventRepository;
import io.kk.bandhub.eventservice.repository.EventSpecification;
import io.kk.bandhub.eventservice.service.EventService;
import io.kk.bandhub.eventservice.utils.TimeRange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final VenueServiceClient venueServiceClient;
    private final Mapper mapper;

    @Transactional
    public void createEvent(EventRequestDTO eventDTO) {

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setBandId(eventDTO.getBandId());
        event.setVenueId(eventDTO.getVenueId());
        event.setDate(eventDTO.getDate());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        event.setTicketPrice(BigDecimal.valueOf(eventDTO.getTicketPrice()));
        event.setDescription(eventDTO.getDescription());

        if (eventDTO.getBookingId() == null) {
            checkEventOverlapping(event);
            mapper.toEventDTOOwnerReg(event, eventDTO.getCreatorId());
        } else {
            event.setBookingId(eventDTO.getBookingId());
            mapper.toEventDTOReg(event, eventDTO.getCreatorId());
        }

        eventRepository.save(event);
    }

    @Transactional
    public EventDTO updateEvent(EventUpdateRequestDTO eventDTO) {
        var existingEvent = eventRepository.findById(eventDTO.getId()).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );

        existingEvent.setName(existingEvent.getName());
        existingEvent.setVenueId(eventDTO.getVenueId());
        existingEvent.setBandId(eventDTO.getBandId());
        existingEvent.setDate(eventDTO.getDate());
        existingEvent.setStartTime(eventDTO.getStartTime());
        existingEvent.setEndTime(eventDTO.getEndTime());
        existingEvent.setDescription(eventDTO.getDescription());
        existingEvent.setTicketPrice(BigDecimal.valueOf(eventDTO.getTicketPrice()));

        if (eventDTO.getBookingId() == null) {
            checkEventOverlapping(existingEvent);
            var mappedEvent = mapper.toEventDTOOwnerReg(existingEvent, eventDTO.getCreatorId());

            eventRepository.save(existingEvent);
            return mappedEvent;
        } else {
            existingEvent.setBookingId(eventDTO.getBookingId());
            var mappedEvent = mapper.toEventDTOReg(existingEvent, eventDTO.getCreatorId());
            eventRepository.save(existingEvent);
            return mappedEvent;
        }
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

    public List<EventDTO> searchEvents(LocalDate date, Integer bandId, List<String> cities) {

        Specification<Event> spec = EventSpecification.hasDate(date)
                .and(EventSpecification.hasBandId(bandId));

        var events = eventRepository.findAll(spec);

        if (cities.isEmpty()) {
            return events.stream().map(mapper::toEventDTO).toList();
        }

        List<VenueDTO> venues;
        try {
            venues = venueServiceClient.getVenuesByIds(events.stream().map(Event::getVenueId).toList());
        } catch (RuntimeException e) {
            return events.stream().map(mapper::toEventDTO).toList();
        }

        Map<Integer, String> venueIdToCity = venues.stream()
                .collect(Collectors.toMap(VenueDTO::getId, VenueDTO::getCity));

        return events.stream()
                .filter(e -> cities.contains(venueIdToCity.get(e.getVenueId())))
                .map(mapper::toEventDTO)
                .toList();
    }

    private void checkEventOverlapping(Event event) {
        var events = eventRepository.findAllByDateAndVenueId(event.getDate(), event.getVenueId());
        var offers = venueServiceClient.getOffersByVenueAndDate(event.getVenueId(), event.getDate());

        Set<TimeRange> timeRanges = events.stream().map(e -> new TimeRange(e.getStartTime(), e.getEndTime())).collect(Collectors.toSet());
        timeRanges.addAll(offers.stream().map(o -> new TimeRange(o.getStartTime(), o.getEndTime())).collect(Collectors.toSet()));

        for (TimeRange timeRange : timeRanges) {
            boolean overlaps = !event.getEndTime().isBefore(timeRange.getStartTime()) &&
                    !event.getStartTime().isAfter(timeRange.getEndTime());

            if (overlaps) {
                throw new RegistrationException("Event time overlaps with other events or existing offers time bounds");
            }
        }
    }
}
