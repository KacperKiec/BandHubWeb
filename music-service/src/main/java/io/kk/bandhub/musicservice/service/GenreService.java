package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.GenreDTO;
import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.exception.genre.GenreError;
import io.kk.bandhub.musicservice.exception.genre.GenreException;
import io.kk.bandhub.musicservice.model.Genre;
import io.kk.bandhub.musicservice.repository.genre.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final Mapper mapper;

    public GenreService(GenreRepository genreRepository, Mapper mapper) {
        this.genreRepository = genreRepository;
        this.mapper = mapper;
    }

    public GenreDTO save(GenreDTO genreDTO) {
        var existingGenre = genreRepository.findByName(genreDTO.getName());
        if (existingGenre.isPresent()) {
            throw new GenreException(GenreError.GENRE_ALREADY_EXISTS);
        }

        Genre genre = new Genre();
        genre.setName(genreDTO.getName());

        return mapper.toGenreDTO(genreRepository.save(genre));
    }

    public void delete(Integer id) {
        var existingGenre = genreRepository.findById(id).orElseThrow(
                () -> new GenreException(GenreError.GENRE_NOT_FOUND)
        );
        genreRepository.deleteById(id);
    }

    public GenreDTO update(GenreDTO genreDTO) {
        var existingGenre = genreRepository.findById(genreDTO.getId()).orElseThrow(
                () -> new GenreException(GenreError.GENRE_NOT_FOUND)
        );
        existingGenre.setName(genreDTO.getName());
        return mapper.toGenreDTO(genreRepository.save(existingGenre));
    }

    public GenreDTO getGenreById(Integer id) {
        var existingGenre = genreRepository.findById(id).orElseThrow(
                () -> new GenreException(GenreError.GENRE_NOT_FOUND)
        );
        return mapper.toGenreDTO(existingGenre);
    }

    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream().map(mapper::toGenreDTO).toList();
    }

    public Page<GenreDTO> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable).map(mapper::toGenreDTO);
    }
}
