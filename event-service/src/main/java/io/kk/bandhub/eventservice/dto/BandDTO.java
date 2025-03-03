package io.kk.bandhub.eventservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BandDTO {
    private Integer id;
    private String name;
    private String bio;
    private Set<String> genres = new HashSet<>();
}
