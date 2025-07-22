package io.kk.bandhub.musicservice.service.impl;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.genre.GenreError;
import io.kk.bandhub.musicservice.exception.genre.GenreException;
import io.kk.bandhub.musicservice.exception.instrument.InstrumentError;
import io.kk.bandhub.musicservice.exception.instrument.InstrumentException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.integration.AccountServiceClient;
import io.kk.bandhub.musicservice.model.BandMusician;
import io.kk.bandhub.musicservice.model.Musician;
import io.kk.bandhub.musicservice.model.MusicianGenre;
import io.kk.bandhub.musicservice.model.MusicianInstrument;
import io.kk.bandhub.musicservice.repository.band_musician.BandMusicianRepository;
import io.kk.bandhub.musicservice.repository.genre.GenreRepository;
import io.kk.bandhub.musicservice.repository.instrument.InstrumentRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianSpecification;
import io.kk.bandhub.musicservice.service.MusicianService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for provide data and methods for MusicianController
 * */
@Service
@RequiredArgsConstructor
public class MusicianServiceImpl implements MusicianService {
    private final MusicianRepository musicianRepository;
    private final GenreRepository genreRepository;
    private final InstrumentRepository instrumentRepository;
    private final BandMusicianRepository bandMusicianRepository;
    private final AccountServiceClient accountServiceClient;
    private final Mapper mapper;

    @Transactional
    public MusicianDTO save(MusicianRequestDTO musicianRequestDTO) {

        var existingMusician = musicianRepository.findMusicianById(musicianRequestDTO.getId());
        if(existingMusician.isPresent()) {
            throw new MusicianException(MusicianError.MUSICIAN_ALREADY_EXISTS);
        }

        Musician musician = new Musician();
        musician.setId(musicianRequestDTO.getId());
        musician.setBio(musicianRequestDTO.getBio());
        musician.setIsLookingBand(musicianRequestDTO.getIsLookingBand());

        if(musicianRequestDTO.getGenres() != null) {
            musician.setMusicianGenres(new HashSet<>());
            for (GenreDTO genre : musicianRequestDTO.getGenres()) {
                var g = genreRepository.findById(genre.getId()).orElseThrow(
                        () -> new GenreException(GenreError.GENRE_NOT_FOUND)
                );
                MusicianGenre musicianGenre = new MusicianGenre(musician, g);
                musician.addMusicianGenre(musicianGenre);
            }
        }

        if(musicianRequestDTO.getInstruments() != null) {
            musician.setMusicianInstruments(new HashSet<>());
            for (InstrumentDTO instrument : musicianRequestDTO.getInstruments()) {
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
    public void updateMusician(MusicianRequestDTO musicianDTO) {
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

        for (MusicianGenre musicianGenre : new HashSet<>(musician.getMusicianGenres())) {
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

        for (MusicianInstrument musicianInstrument : new HashSet<>(musician.getMusicianInstruments())) {
            boolean instrumentExistsInDTO = musicianDTO.getInstruments().stream()
                    .anyMatch(instrument -> musicianInstrument.getInstrument().getId().equals(instrument.getId()));

            if (!instrumentExistsInDTO) {
                musician.removeMusicianInstrument(musicianInstrument);
            }
        }

        musicianRepository.save(musician);
    }

    public Page<MusicianDTO> getAll(Pageable pageable) {
        return musicianRepository.findAll(pageable).map(mapper::toMusicianDTO);
    }

    public MusicianDTO getById(Integer id) {
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

    public List<BandDTO> getMusicianBands(Integer id) {
        var bands = bandMusicianRepository.getBandMusiciansByMusicianId(id).stream().map(BandMusician::getBand).toList();
        return bands.stream().map(mapper::toBandDTO).toList();
    }

    @Transactional
    public void deleteMusician(Integer musicianId) {
        musicianRepository.findMusicianById(musicianId).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );
        musicianRepository.deleteMusicianById(musicianId);
    }
}
