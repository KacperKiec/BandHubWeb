package io.kk.bandhub.eventservice.service;

import io.kk.bandhub.eventservice.dto.FollowRequestDTO;
import io.kk.bandhub.eventservice.dto.UserEventDTO;

import java.util.List;

public interface UserEventService {
    List<UserEventDTO> getUserEvents(Integer userId);
    void followEvent(FollowRequestDTO followRequest);
    void unfollowEvent(FollowRequestDTO followRequest);
}
