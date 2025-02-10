package io.kk.bandhub.musicservice.dto;

import io.kk.bandhub.musicservice.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Mapper {

    public BandDTO toBandDTO(Band band) {
        BandDTO dto = new BandDTO();
        dto.setId(band.getId());
        dto.setName(band.getName());
        dto.setCity(band.getCity());
        dto.setBio(band.getBio());
        dto.setLookingMembers(band.getIsLookingMembers());

        var genres = band.getBandGenres().stream().map(BandGenre::getGenre).collect(Collectors.toSet());
        dto.setGenres(genres);

        var musicians = band.getBandMusicians().stream().map(BandMusician::getMusician).map(this::toMusicianDTO).collect(Collectors.toSet());
        dto.setMusicians(musicians);

        return dto;
    }

    public MusicianDTO toMusicianDTO(Musician musician) {
        MusicianDTO dto = new MusicianDTO();
        dto.setId(musician.getId());
        dto.setBio(musician.getBio());
        dto.setLookingBand(musician.getIsLookingBand());

        var genres = musician.getMusicianGenres().stream().map(MusicianGenre::getGenre).collect(Collectors.toSet());
        dto.setGenres(genres);

        var instruments = musician.getMusicianInstruments().stream().map(MusicianInstrument::getInstrument).collect(Collectors.toSet());
        dto.setInstruments(instruments);

        return dto;
    }
}
