package io.kk.bandhub.eventservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowRequestDTO {
    private Integer userId;
    private Integer eventId;
}
