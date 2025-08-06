package io.kk.bandhub.eventservice.service.impl;

import io.kk.bandhub.eventservice.dto.*;
import io.kk.bandhub.eventservice.exception.EventError;
import io.kk.bandhub.eventservice.exception.EventException;
import io.kk.bandhub.eventservice.exception.RegistrationException;
import io.kk.bandhub.eventservice.integration.MusicServiceClient;
import io.kk.bandhub.eventservice.integration.RabbitService;
import io.kk.bandhub.eventservice.integration.VenueServiceClient;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.model.UserEvent;
import io.kk.bandhub.eventservice.repository.event.EventRepository;
import io.kk.bandhub.eventservice.repository.event.EventSpecification;
import io.kk.bandhub.eventservice.repository.user_event.UserEventRepository;
import io.kk.bandhub.eventservice.service.EventService;
import io.kk.bandhub.eventservice.utils.NotificationCreator;
import io.kk.bandhub.eventservice.utils.NotificationType;
import io.kk.bandhub.eventservice.utils.RelationType;
import io.kk.bandhub.eventservice.utils.TimeRange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserEventRepository userEventRepository;
    private final VenueServiceClient venueServiceClient;
    private final MusicServiceClient musicServiceClient;
    private final RabbitService rabbitService;
    private final Mapper mapper;

    @Transactional
    public UserEventDTO createEvent(EventRequestDTO eventDTO) {

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
            var dto = buildOwnerEvent(event, eventDTO.getCreatorId());

            var e = eventRepository.save(event);
            saveUserEventsAndSendNotifications(event, dto);

            return mapper.buildUserEventDTO(userEventRepository.findByUserIdAndEventId(eventDTO.getCreatorId(), e.getId()).orElseThrow(
                    () -> new EventException(EventError.EVENT_NOT_FOUND)
            ));

        } else {
            event.setBookingId(eventDTO.getBookingId());
            var dto = buildEvent(event, eventDTO.getCreatorId());

            var e = eventRepository.save(event);

            userEventRepository.save(buildUserEvent(event, dto.getOwnerId(), RelationType.LANDLORD));
            String title = "Nowe wydarzenie";
            String content = "Zespół %s wynajmujący twój lokal utworzył nowe wydarzenie, sprawdź swój harmonogram.".formatted(dto.getBandName());
            var notificationRequestDTO = NotificationCreator.createNotification(NotificationType.NEW_EVENT, dto.getOwnerId(), title, content);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("bandId", dto.getBandId());
            notificationRequestDTO.setMetadata(metadata);
            rabbitService.sendNotification(notificationRequestDTO);

            saveUserEventsAndSendNotifications(event, dto);

            return mapper.buildUserEventDTO(userEventRepository.findByUserIdAndEventId(eventDTO.getCreatorId(), e.getId()).orElseThrow(
                    () -> new EventException(EventError.EVENT_NOT_FOUND)
            ));
        }
    }

    @Transactional
    public UserEventDTO updateEvent(EventUpdateRequestDTO eventDTO) {
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
            buildEvent(existingEvent, eventDTO.getCreatorId());

            var event = eventRepository.save(existingEvent);
            return mapper.buildUserEventDTO(userEventRepository.findByUserIdAndEventId(eventDTO.getCreatorId(), event.getId()).orElseThrow(
                    () -> new EventException(EventError.EVENT_NOT_FOUND)
            ));
        } else {
            existingEvent.setBookingId(eventDTO.getBookingId());
            buildEvent(existingEvent, eventDTO.getCreatorId());

            var event = eventRepository.save(existingEvent);
            return mapper.buildUserEventDTO(userEventRepository.findByUserIdAndEventId(eventDTO.getCreatorId(), event.getId()).orElseThrow(
                    () -> new EventException(EventError.EVENT_NOT_FOUND)
            ));
        }
    }

    @Transactional
    public void deleteEvent(Integer eventId) {
        eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );

        var userEvents = userEventRepository.findAllByEventId(eventId);
        userEvents.forEach(e -> userEventRepository.deleteById(e.getId()));

        eventRepository.deleteById(eventId);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(e -> {
            var venueEventData = venueServiceClient.getVenueById(e.getVenueId());
            var bandEventData = musicServiceClient.getBand(e.getBandId());

            return mapper.buildEventDTO(e, venueEventData, bandEventData, userEventRepository.findAllByEventId(e.getId()).size());
        }).toList();
    }

    public EventDTO getEventById(Integer eventId) {
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );

        var venueEventData = venueServiceClient.getVenueById(event.getVenueId());
        var bandEventData = musicServiceClient.getBand(event.getBandId());

        return mapper.buildEventDTO(event, venueEventData, bandEventData, userEventRepository.findAllByEventId(event.getId()).size());
    }

    public List<EventDTO> searchEvents(LocalDate date, Integer bandId, List<String> cities) {

        Specification<Event> spec = EventSpecification.hasDate(date)
                .and(EventSpecification.hasBandId(bandId));

        var events = eventRepository.findAll(spec);

        if (cities.isEmpty()) {
            return events.stream().map(e -> {
                var venueEventData = venueServiceClient.getVenueById(e.getVenueId());
                var bandEventData = musicServiceClient.getBand(e.getBandId());

                return mapper.buildEventDTO(e, venueEventData, bandEventData, userEventRepository.findAllByEventId(e.getId()).size());
            }).toList();
        }

        List<VenueDTO> venues;
        try {
            venues = venueServiceClient.getVenuesByIds(events.stream().map(Event::getVenueId).toList());
        } catch (RuntimeException ex) {
            return events.stream().map(e -> {
                var venueEventData = venueServiceClient.getVenueById(e.getVenueId());
                var bandEventData = musicServiceClient.getBand(e.getBandId());

                return mapper.buildEventDTO(e, venueEventData, bandEventData, userEventRepository.findAllByEventId(e.getId()).size());
            }).toList();
        }

        Map<Integer, String> venueIdToCity = venues.stream()
                .collect(Collectors.toMap(VenueDTO::getId, VenueDTO::getCity));

        return events.stream()
                .filter(e -> cities.contains(venueIdToCity.get(e.getVenueId())))
                .map(e -> {
                    var venueEventData = venueServiceClient.getVenueById(e.getVenueId());
                    var bandEventData = musicServiceClient.getBand(e.getBandId());

                    return mapper.buildEventDTO(e, venueEventData, bandEventData, userEventRepository.findAllByEventId(e.getId()).size());
                }).toList();
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

    private EventDTO buildEvent(Event event, Integer creatorId) {
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
                .ownerId(venueEventData.getOwnerId())
                .street(venueEventData.getStreet())
                .number(venueEventData.getNumber())
                .city(venueEventData.getCity())
                .postalCode(venueEventData.getPostalCode())
                .country(venueEventData.getCountry())
                .build();

        return mapper.buildEventDTO(event, venue, bandData, userEventRepository.findAllByEventId(event.getId()).size());
    }

    private EventDTO buildOwnerEvent(Event event, Integer creatorId) {
        var venueData = venueServiceClient.getVenueById(event.getVenueId());
        var bandData = musicServiceClient.getBandReg(event.getBandId());

        if (!creatorId.equals(venueData.getOwnerId())) {
            throw new RegistrationException("Only venue owner can create this event");
        }

        if (!creatorId.equals(bandData.getFounderId())) {
            throw new RegistrationException("Creator must be the band founder");
        }

        return mapper.buildEventDTO(event, venueData, bandData, userEventRepository.findAllByEventId(event.getId()).size());
    }

    private UserEvent buildUserEvent(Event event, Integer userId, RelationType relation) {
        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(userId);
        userEvent.setEvent(event);
        userEvent.setRelation(relation);
        return userEvent;
    }

    private void saveUserEventsAndSendNotifications(Event event, EventDTO dto) {
        var list = musicServiceClient.getBandMembersIds(event.getBandId());
        for (Integer userId : list) {
            userEventRepository.save(buildUserEvent(event, userId, RelationType.PARTICIPANT));

            String title = "Nowe wydarzenie";
            String content = "Zespół %s ma nowe wydarzenie muzyczne, sprawdź swój harmonogram.".formatted(dto.getBandName());

            var notificationRequestDTO = NotificationCreator.createNotification(NotificationType.NEW_EVENT, userId, title, content);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("bandId", dto.getBandId());
            notificationRequestDTO.setMetadata(metadata);

            rabbitService.sendNotification(notificationRequestDTO);
        }
    }
}
