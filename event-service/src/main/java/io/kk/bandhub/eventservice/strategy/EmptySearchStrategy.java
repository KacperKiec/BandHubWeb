package io.kk.bandhub.eventservice.strategy;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.dto.Mapper;
import io.kk.bandhub.eventservice.integration.MusicServiceClient;
import io.kk.bandhub.eventservice.integration.VenueServiceClient;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.repository.event.EventRepository;
import io.kk.bandhub.eventservice.repository.event.EventSpecification;
import io.kk.bandhub.eventservice.repository.user_event.UserEventRepository;
import io.kk.bandhub.eventservice.utils.SearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmptySearchStrategy implements EventSearchStrategy {

    private final EventRepository eventRepository;
    private final VenueServiceClient venueServiceClient;
    private final MusicServiceClient musicServiceClient;
    private final UserEventRepository userEventRepository;
    private final Mapper mapper;

    @Override
    public Page<EventDTO> search(SearchParams params, Pageable pageable) {
        Specification<Event> spec = Specification.where(EventSpecification.isActual());
        return eventRepository.findAll(spec, pageable).map(e -> {
            var venueEventData = venueServiceClient.getVenueById(e.getVenueId());
            var band = musicServiceClient.getBandById(e.getBandId());
            return mapper.buildEventDTO(e, venueEventData, band, userEventRepository.findAllByEventId(e.getId()).size());
        });
    }
}
