package io.kk.bandhub.eventservice.service.impl;

import io.kk.bandhub.eventservice.dto.FollowRequestDTO;
import io.kk.bandhub.eventservice.dto.Mapper;
import io.kk.bandhub.eventservice.dto.UserEventDTO;
import io.kk.bandhub.eventservice.exception.EventError;
import io.kk.bandhub.eventservice.exception.EventException;
import io.kk.bandhub.eventservice.model.UserEvent;
import io.kk.bandhub.eventservice.repository.event.EventRepository;
import io.kk.bandhub.eventservice.repository.user_event.UserEventRepository;
import io.kk.bandhub.eventservice.service.UserEventService;
import io.kk.bandhub.eventservice.utils.RelationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {
    private final UserEventRepository userEventRepository;
    private final EventRepository eventRepository;
    private final Mapper mapper;

    public List<UserEventDTO> getUserEvents(Integer userId) {
        return userEventRepository.findAllByUserId(userId)
                .stream().map(mapper::buildUserEventDTO).toList();
    }

    public UserEventDTO followEvent(FollowRequestDTO followRequest) {

        var existingFollowing = userEventRepository.findByUserIdAndEventId(followRequest.getUserId(), followRequest.getEventId());

        if (existingFollowing.isPresent()) {
            throw new EventException(EventError.EVENT_ALREADY_FOLLOWED);
        }

        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(followRequest.getUserId());

        var ue = eventRepository.findById(followRequest.getEventId()).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );

        userEvent.setEvent(ue);
        userEvent.setRelation(RelationType.FOLLOWER);
        return mapper.buildUserEventDTO(userEventRepository.save(userEvent));
    }

    public void unfollowEvent(FollowRequestDTO followRequest) {
        var ue = userEventRepository.findByUserIdAndEventId(followRequest.getUserId(), followRequest.getEventId()).orElseThrow(
                () -> new EventException(EventError.EVENT_NOT_FOUND)
        );
        userEventRepository.deleteById(ue.getId());
    }
}
