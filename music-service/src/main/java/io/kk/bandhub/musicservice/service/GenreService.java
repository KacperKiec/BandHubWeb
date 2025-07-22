package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    GenreDTO save(GenreDTO genreDTO);
    void delete(Integer id);
    GenreDTO update(GenreDTO genreDTO);
    GenreDTO getGenreById(Integer id);
    List<GenreDTO> getAllGenres();
}
