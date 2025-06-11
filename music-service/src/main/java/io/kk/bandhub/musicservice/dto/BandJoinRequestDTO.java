package io.kk.bandhub.musicservice.dto;

import io.kk.bandhub.musicservice.model.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BandJoinRequestDTO {
    private Integer id;
    private Integer bandId;
    private String bandName;
    private Integer applicantId;
    private String applicantFirstName;
    private String applicantLastName;
    private RequestStatus status;
    private LocalDate creationDate;
}
