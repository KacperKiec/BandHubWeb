package io.kk.bandhub.musicservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandMembershipRequestDTO {
    private Integer bandId;
    private Integer musicianId;
}
