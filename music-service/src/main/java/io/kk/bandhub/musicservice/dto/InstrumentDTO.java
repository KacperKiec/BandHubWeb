package io.kk.bandhub.musicservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstrumentDTO {
    private Integer id;

    @Size(max = 40)
    @NotNull
    private String name;
}
