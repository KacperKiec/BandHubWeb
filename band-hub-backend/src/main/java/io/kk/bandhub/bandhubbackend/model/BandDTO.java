package io.kk.bandhub.bandhubbackend.model;

import lombok.Data;

import java.util.List;

@Data
public class BandDTO {
    private int id;
    private String bandName;
    private int maxMembers;
    private List<Instrument> requestedInstruments;
    private List<Instrument> currentInstruments;
    private List<MusicalGenre> genres;
    private String founderName;
    private List<String> members;
    private boolean isLookingMembers;
    private String city;
    private String bio;
}
