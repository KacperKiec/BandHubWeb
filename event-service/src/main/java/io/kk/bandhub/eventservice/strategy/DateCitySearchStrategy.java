package io.kk.bandhub.eventservice.strategy;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.dto.Mapper;
import io.kk.bandhub.eventservice.dto.VenueDTO;
import io.kk.bandhub.eventservice.integration.MusicServiceClient;
import io.kk.bandhub.eventservice.integration.VenueServiceClient;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.repository.event.EventRepository;
import io.kk.bandhub.eventservice.repository.event.EventSpecification;
import io.kk.bandhub.eventservice.repository.user_event.UserEventRepository;
import io.kk.bandhub.eventservice.utils.SearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DateCitySearchStrategy implements EventSearchStrategy {

    private final EventRepository eventRepository;
    private final VenueServiceClient venueServiceClient;
    private final MusicServiceClient musicServiceClient;
    private final UserEventRepository userEventRepository;
    private final Mapper mapper;

    @Override
    public Page<EventDTO> search(SearchParams params, Pageable pageable) {
        Specification<Event> spec = Specification.where(EventSpecification.hasDate(params.getDate()));

        var listEvents = eventRepository.findAll(spec);
        var venues = venueServiceClient.getVenuesByIds(listEvents.stream().map(Event::getVenueId).toList());

        if (venues.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        Map<Integer, String> venueIdToCity = venues.stream()
                .collect(Collectors.toMap(VenueDTO::getId, VenueDTO::getCity));

        List<EventDTO> filteredEvents = listEvents.stream()
                .filter(e -> params.getCities().contains(venueIdToCity.get(e.getVenueId())))
                .map(e -> {
                    var venue = venues.stream().filter(v -> v.getId().equals(e.getVenueId())).findFirst().orElseThrow();
                    var bandEventData = musicServiceClient.getBandById(e.getBandId());
                    return mapper.buildEventDTO(e, venue, bandEventData, userEventRepository.findAllByEventId(e.getId()).size());
                }).toList();

        int total = filteredEvents.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<EventDTO> pagedEvents = start > total ? List.of() : filteredEvents.subList(start, end);

        return new PageImpl<>(pagedEvents, pageable, total);
    }
}
