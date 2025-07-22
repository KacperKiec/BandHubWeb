package io.kk.bandhub.eventservice.repository;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    void deleteById(Integer id);
    Optional<Event> findById(Integer id);
    List<Event> findAll();
    List<Event> findAll(Specification<Event> spec);
}
