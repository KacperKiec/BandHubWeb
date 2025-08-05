package io.kk.bandhub.eventservice.repository.user_event;

import io.kk.bandhub.eventservice.model.UserEvent;

import java.util.List;
import java.util.Optional;

public interface UserEventRepository {
    UserEvent save(UserEvent userEvent);
    void deleteById(Integer id);
    Optional<UserEvent> findById(Integer id);
    List<UserEvent> findAllByUserId(Integer userId);
    List<UserEvent> findAllByEventId(Integer eventId);
    Optional<UserEvent> findByUserIdAndEventId(Integer userId, Integer eventId);
}
