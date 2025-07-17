package io.kk.bandhub.musicservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BandRequestDTO {
    @Size(max = 30, message = "Band name cannot exceed 30 characters")
    @NotNull(message = "Band name cannot be null")
    @NotBlank
    private String name;

    @Size(max = 20, message = "City name cannot exceed 20 characters")
    private String city;

    @Size(max = 50, message = "City name cannot exceed 50 characters")
    private String country;

    @Size(max = 400, message = "Bio cannot exceed 400 characters")
    @NotBlank
    private String bio;

    @NotNull(message = "Looking members flag cannot be null")
    private boolean isLookingMembers;

    @NotNull
    private Set<GenreDTO> genres = new HashSet<>();

    @NotNull
    private Integer founderId;
}
