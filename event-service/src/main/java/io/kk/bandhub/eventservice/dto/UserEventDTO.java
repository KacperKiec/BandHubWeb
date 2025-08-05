package io.kk.bandhub.eventservice.dto;

import io.kk.bandhub.eventservice.utils.RelationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class UserEventDTO {
    private Integer id;
    private Integer userId;
    private Integer eventId;
    private String eventName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RelationType relation;
}
