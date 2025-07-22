package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.dto.MusicianRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MusicianService {
    MusicianDTO save(MusicianRequestDTO musicianRequestDTO);
    void updateMusician(MusicianRequestDTO musicianDTO);
    Page<MusicianDTO> getAll(Pageable pageable);
    MusicianDTO getById(Integer id);
    Page<MusicianDTO> findMusiciansByFilters(List<String> genres, List<String> instruments, Boolean isLookingForBand, Pageable pageable, String firstName, String lastName, Integer minAge, Integer maxAge, List<String> cities);
    List<BandDTO> getMusicianBands(Integer id);
    void deleteMusician(Integer musicianId);
}
