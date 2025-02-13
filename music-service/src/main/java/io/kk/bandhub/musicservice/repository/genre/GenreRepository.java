package io.kk.bandhub.musicservice.repository.genre;

import io.kk.bandhub.musicservice.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);
    void deleteById(Integer id);
    Optional<Genre> findById(Integer id);
    Optional<Genre> findByName(String name);
    List<Genre> findAll();
    Page<Genre> findAll(Pageable pageable);
}
