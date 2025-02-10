package io.kk.bandhub.musicservice.dto;

import io.kk.bandhub.musicservice.model.Genre;
import io.kk.bandhub.musicservice.model.Instrument;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class MusicianDTO {
    private Integer id;
    private String first_name;
    private String last_name;
    private LocalDate birthday;
    private String gender;
    private String city;

    @Size(max = 255, message = "Bio cannot exceed 400 characters")
    private String bio;

    @NotNull(message = "Looking band flag cannot be null")
    private boolean isLookingBand;

    private Set<Genre> genres;
    private Set<Instrument> instruments;
}
