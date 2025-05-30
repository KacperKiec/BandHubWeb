package io.kk.bandhub.musicservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandMembershipRequestDTO {
    @NotNull
    private Integer bandId;

    @NotNull
    private Integer musicianId;
}
