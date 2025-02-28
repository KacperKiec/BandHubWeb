package io.kk.bandhub.eventservice.repository;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventRepository extends JpaRepository<Event, Integer>, EventRepository, JpaSpecificationExecutor<Event> {

}
