package io.kk.bandhub.musicservice.dto;

import io.kk.bandhub.musicservice.model.*;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.integration.AccountServiceClient;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Mapper {

    private final AccountServiceClient accountServiceClient;
    private final BandRepository bandRepository;

    public Mapper(AccountServiceClient accountServiceClient, BandRepository bandRepository) {
        this.accountServiceClient = accountServiceClient;
        this.bandRepository = bandRepository;
    }

    public GenreDTO toGenreDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        return genreDTO;
    }

    public InstrumentDTO toInstrumentDTO(Instrument instrument) {
        InstrumentDTO instrumentDTO = new InstrumentDTO();
        instrumentDTO.setId(instrument.getId());
        instrumentDTO.setName(instrument.getName());
        return instrumentDTO;
    }

    public BandDTO toBandDTO(Band band) {
        BandDTO dto = new BandDTO();
        dto.setId(band.getId());
        dto.setName(band.getName());
        dto.setCity(band.getCity());
        dto.setCountry(band.getCountry());
        dto.setBio(band.getBio());
        dto.setLookingMembers(band.getIsLookingMembers());
        dto.setFounderId(bandRepository.getFounderId(band.getId()));

        var genres = band.getBandGenres().stream().map(BandGenre::getGenre).map(this::toGenreDTO).collect(Collectors.toSet());
        dto.setGenres(genres);

        var musicians = band.getBandMusicians().stream().map(BandMusician::getMusician).map(this::toMusicianDTO).collect(Collectors.toSet());
        dto.setMusicians(musicians);

        return dto;
    }

    public BandEventDataDTO toBandEvtDTO(Band band, Integer founderId) {
        BandEventDataDTO dto = new BandEventDataDTO();
        dto.setId(band.getId());
        dto.setFounderId(founderId);
        dto.setName(band.getName());
        return dto;
    }

    public MusicianDTO toMusicianDTO(Musician musician) {
        AccountDTO account = accountServiceClient.getAccount(musician.getId());

        return setDTOFields(musician, account);
    }

    public MusicianDTO toMusicianDTOReg(Musician musician) {
        AccountDTO account = accountServiceClient.getAccountInRegistration(musician.getId());

        return setDTOFields(musician, account);
    }

    public BandInvitationDTO toBandInvitationDTO(BandInvitation bandInvitation) {
        BandInvitationDTO dto = new BandInvitationDTO();
        dto.setId(bandInvitation.getId());
        dto.setBandId(bandInvitation.getBand().getId());
        dto.setStatus(bandInvitation.getStatus());
        dto.setCreationDate(bandInvitation.getCreatedAt());
        dto.setInvitedMusicianId(bandInvitation.getMusician().getId());
        dto.setBandName(bandInvitation.getBand().getName());
        return dto;
    }

    public BandJoinRequestDTO toBandRequestDTO(BandJoinRequest bandJoinRequest) {
        BandJoinRequestDTO dto = new BandJoinRequestDTO();
        dto.setId(bandJoinRequest.getId());
        dto.setBandId(bandJoinRequest.getBand().getId());
        dto.setStatus(bandJoinRequest.getStatus());
        dto.setCreationDate(bandJoinRequest.getCreatedAt());
        dto.setApplicantId(bandJoinRequest.getMusician().getId());
        dto.setBandName(bandJoinRequest.getBand().getName());

        AccountDTO account = accountServiceClient.getAccount(bandJoinRequest.getMusician().getId());
        dto.setApplicantFirstName(account.getFirstName());
        dto.setApplicantLastName(account.getLastName());

        return dto;
    }

    private MusicianDTO setDTOFields(Musician musician, AccountDTO account) {
        MusicianDTO dto = new MusicianDTO();
        dto.setId(musician.getId());
        dto.setBio(musician.getBio());
        dto.setIsLookingBand(musician.getIsLookingBand());

        if(musician.getMusicianGenres() != null) {
            var genres = musician.getMusicianGenres().stream()
                    .map(MusicianGenre::getGenre)
                    .collect(Collectors.toSet());
            dto.setGenres(genres.stream().map(this::toGenreDTO).collect(Collectors.toSet()));
        }

        if(musician.getMusicianInstruments() != null) {
            var instruments = musician.getMusicianInstruments().stream()
                    .map(MusicianInstrument::getInstrument)
                    .collect(Collectors.toSet());
            dto.setInstruments(instruments.stream().map(this::toInstrumentDTO).collect(Collectors.toSet()));
        }


        if (account != null) {
            dto.setFirstName(account.getFirstName());
            dto.setLastName(account.getLastName());
            dto.setBirthday(account.getBirthDate());
            dto.setGender(account.getGender() != null ? (account.getGender() ? "Male" : "Female") : null);
            dto.setCity(account.getCity());
            dto.setCountry(account.getCountry());
        }

        return dto;
    }

}
