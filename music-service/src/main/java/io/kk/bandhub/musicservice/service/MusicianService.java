package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.genre.GenreError;
import io.kk.bandhub.musicservice.exception.genre.GenreException;
import io.kk.bandhub.musicservice.exception.instrument.InstrumentError;
import io.kk.bandhub.musicservice.exception.instrument.InstrumentException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.model.*;
import io.kk.bandhub.musicservice.repository.genre.GenreRepository;
import io.kk.bandhub.musicservice.repository.instrument.InstrumentRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for provide data and methods for MusicianController
 * */
@Service
public class MusicianService {
    private final MusicianRepository musicianRepository;
    private final GenreRepository genreRepository;
    private final InstrumentRepository instrumentRepository;
    private final AccountServiceClient accountServiceClient;
    private final Mapper mapper;

    public MusicianService(MusicianRepository musicianRepository, GenreRepository genreRepository, InstrumentRepository instrumentRepository, AccountServiceClient accountServiceClient, Mapper mapper) {
        this.musicianRepository = musicianRepository;
        this.genreRepository = genreRepository;
        this.instrumentRepository = instrumentRepository;
        this.accountServiceClient = accountServiceClient;
        this.mapper = mapper;
    }

    @Transactional
    public MusicianDTO save(MusicianDTO musicianDTO) {

        var existingMusician = musicianRepository.findMusicianById(musicianDTO.getId());
        if(existingMusician.isPresent()) {
            throw new MusicianException(MusicianError.MUSICIAN_ALREADY_EXISTS);
        }

        Musician musician = new Musician();
        musician.setId(musicianDTO.getId());
        musician.setBio(musicianDTO.getBio());
        musician.setIsLookingBand(musicianDTO.getIsLookingBand());

        if(musicianDTO.getGenres() != null) {
            for (GenreDTO genre : musicianDTO.getGenres()) {
                var g = genreRepository.findById(genre.getId()).orElseThrow(
                        () -> new GenreException(GenreError.GENRE_NOT_FOUND)
                );
                MusicianGenre musicianGenre = new MusicianGenre(musician, g);
                musician.addMusicianGenre(musicianGenre);
            }
        }

        if(musicianDTO.getInstruments() != null) {
            for (InstrumentDTO instrument : musicianDTO.getInstruments()) {
                var i = instrumentRepository.findById(instrument.getId()).orElseThrow(
                        () -> new InstrumentException(InstrumentError.INSTRUMENT_NOT_FOUND)
                );
                MusicianInstrument musicianInstrument = new MusicianInstrument(musician, i);
                musician.addMusicianInstrument(musicianInstrument);
            }
        }

        var mappedMusician = mapper.toMusicianDTOReg(musician);
        musicianRepository.save(musician);

        return mappedMusician;
    }

    @Transactional
    public MusicianDTO updateMusician(MusicianDTO musicianDTO) {
        var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                () ->new MusicianException(MusicianError.MUSICIAN_NOT_FOUND));

        musician.setId(musicianDTO.getId());
        musician.setBio(musicianDTO.getBio());
        musician.setIsLookingBand(musicianDTO.getIsLookingBand());

        for (GenreDTO genre : musicianDTO.getGenres()) {
            var g = genreRepository.findById(genre.getId()).orElseThrow(
                    () -> new GenreException(GenreError.GENRE_NOT_FOUND)
            );
            boolean genreExistsInMusician = musician.getMusicianGenres().stream()
                    .anyMatch(musicianGenre -> musicianGenre.getGenre().getId().equals(genre.getId()));

            if (!genreExistsInMusician) {
                MusicianGenre newMusicianGenre = new MusicianGenre(musician, g);
                musician.addMusicianGenre(newMusicianGenre);
            }
        }

        for (MusicianGenre musicianGenre : musician.getMusicianGenres()) {
            boolean genreExistsInDTO = musicianDTO.getGenres().stream()
                    .anyMatch(genre -> musicianGenre.getGenre().getId().equals(genre.getId()));

            if (!genreExistsInDTO) {
                musician.removeMusicianGenre(musicianGenre);
            }
        }

        for (InstrumentDTO instrument : musicianDTO.getInstruments()) {
            var i = instrumentRepository.findById(instrument.getId()).orElseThrow(
                    () -> new InstrumentException(InstrumentError.INSTRUMENT_NOT_FOUND)
            );
            boolean instrumentExistsInMusician = musician.getMusicianInstruments().stream()
                    .anyMatch(musicianInstrument -> musicianInstrument.getInstrument().getId().equals(instrument.getId()));

            if (!instrumentExistsInMusician) {
                MusicianInstrument newMusicianInstrument = new MusicianInstrument(musician, i);
                musician.addMusicianInstrument(newMusicianInstrument);
            }
        }

        for (MusicianInstrument musicianInstrument : musician.getMusicianInstruments()) {
            boolean instrumentExistsInDTO = musicianDTO.getInstruments().stream()
                    .anyMatch(instrument -> musicianInstrument.getInstrument().getId().equals(instrument.getId()));

            if (!instrumentExistsInDTO) {
                musician.removeMusicianInstrument(musicianInstrument);
            }
        }

        return mapper.toMusicianDTO(musicianRepository.save(musician));
    }

    public Page<MusicianDTO> getAll(Pageable pageable) {
        return musicianRepository.findAll(pageable).map(mapper::toMusicianDTO);
    }

    public MusicianDTO getById(int id) {
        var musician = musicianRepository.findMusicianById(id)
                .orElseThrow(() -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND));
        return mapper.toMusicianDTO(musician);
    }

    public Page<MusicianDTO> findMusiciansByFilters(
            List<String> genres,
            List<String> instruments,
            Boolean isLookingForBand,
            Pageable pageable,
            String firstName,
            String lastName,
            Integer minAge,
            Integer maxAge,
            List<String> cities) {

        Specification<Musician> spec = Specification.where(MusicianSpecification.hasGenres(genres))
                .and(MusicianSpecification.hasInstruments(instruments))
                .and(MusicianSpecification.isLookingForBand(isLookingForBand));

        var musicianResult = musicianRepository.findAll(spec, pageable).map(mapper::toMusicianDTO);

        if(firstName == null && lastName == null && minAge == null && maxAge == null && (cities == null || cities.isEmpty())) {
            return musicianResult;
        }

        Page<AccountDTO> accountResult;
        try {
            accountResult = accountServiceClient.getAccountsWithParams(firstName, lastName, minAge, maxAge, cities);
        } catch (Exception e) {
            return musicianResult;
        }

        Map<Integer, AccountDTO> accountMap = accountResult.getContent().stream()
                .collect(Collectors.toMap(AccountDTO::getId, account -> account));

        List<MusicianDTO> enrichedMusicians = musicianResult.getContent().stream()
                .filter(musician -> accountMap.containsKey(musician.getId()))
                .peek(musician -> {
                    AccountDTO account = accountMap.get(musician.getId());
                    musician.setFirstName(account.getFirstName());
                    musician.setLastName(account.getLastName());
                    musician.setBirthday(account.getBirthDate());
                    musician.setGender(account.getGender() != null ? (account.getGender() ? "Male" : "Female") : null);
                    musician.setCity(account.getCity());
                })
                .collect(Collectors.toList());

        return new PageImpl<>(enrichedMusicians, pageable, enrichedMusicians.size());
    }

    @Transactional
    public void deleteMusician(int musicianId) {
        var musician = musicianRepository.findMusicianById(musicianId).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );
        musicianRepository.deleteMusicianById(musicianId);
    }
}
