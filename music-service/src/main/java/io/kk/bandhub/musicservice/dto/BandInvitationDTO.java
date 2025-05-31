package io.kk.bandhub.musicservice.dto;

import io.kk.bandhub.musicservice.model.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BandInvitationDTO {
    @NotNull
    private Integer id;
    @NotNull
    private Integer bandId;
    @NotNull
    private Integer invitedMusicianId;
    @NotNull
    private RequestStatus status;
    private LocalDate creationDate;
}
