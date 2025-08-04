package io.kk.bandhub.eventservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class OfferDTO {
    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;
}
