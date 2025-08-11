package io.kk.bandhub.eventservice.repository.event;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    void deleteById(Integer id);
    Optional<Event> findById(Integer id);
    List<Event> findAll();
    List<Event> findAllByDateAndVenueId(LocalDate date, Integer venueId);
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);
    List<Event> findAll(Specification<Event> spec);
}
