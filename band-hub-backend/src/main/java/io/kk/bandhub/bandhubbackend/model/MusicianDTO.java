package io.kk.bandhub.bandhubbackend.model;

import lombok.Data;

import java.util.List;

@Data
public class MusicianDTO {
    private int id;
    private String email;
    private String password;
    private String name;
    private String city;
    private List<Instrument> instruments;
    private List<MusicalGenre> genres;
    private Sex sex;
    private String bio;
    private boolean isLookingBand;
    private List<String> bands;
}
