package io.kk.bandhub.musicservice.service.impl;

import io.kk.bandhub.musicservice.dto.GenreDTO;
import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.exception.genre.GenreError;
import io.kk.bandhub.musicservice.exception.genre.GenreException;
import io.kk.bandhub.musicservice.model.Genre;
import io.kk.bandhub.musicservice.repository.genre.GenreRepository;
import io.kk.bandhub.musicservice.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final Mapper mapper;

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
        genreRepository.findById(id).orElseThrow(
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
        return genreRepository.findAll().stream()
                .map(mapper::toGenreDTO)
                .sorted(Comparator.comparing(GenreDTO::getName))
                .toList();
    }
}
