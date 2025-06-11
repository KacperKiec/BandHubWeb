package io.kk.bandhub.musicservice.dto;

import io.kk.bandhub.musicservice.model.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandInvitationUpdateRequestDTO {
    @NotNull
    private Integer id;
    @NotNull
    private Integer bandId;
    @NotNull
    private Integer invitedMusicianId;
    @NotNull
    private RequestStatus status;
}
