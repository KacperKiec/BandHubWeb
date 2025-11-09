package io.kk.bandhub.musicservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MusicianRequestDTO {
    @NotNull(message = "Id cannot be null")
    private Integer id;

    @Size(max = 255, message = "Bio cannot exceed 400 characters")
    private String bio;

    @NotNull(message = "Looking band flag cannot be null")
    @JsonProperty("isLookingBand")
    private Boolean isLookingBand;

    private Set<GenreDTO> genres;
    private Set<InstrumentDTO> instruments;
}
