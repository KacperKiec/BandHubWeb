package io.kk.bandhub.eventservice.repository.user_event;

import io.kk.bandhub.eventservice.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserEventRepository extends JpaRepository<UserEvent, Integer>, UserEventRepository {

}
