package io.kk.bandhub.eventservice.repository;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    void deleteById(int id);
    Optional<Event> findById(int id);
    Optional<Event> findByVenueIdAndOrganizerIdAndDateAndStartTimeAndName(Integer venueId, Integer organizerId, LocalDate date, LocalTime startTime, String name);
    List<Event> findAll();
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);
}
