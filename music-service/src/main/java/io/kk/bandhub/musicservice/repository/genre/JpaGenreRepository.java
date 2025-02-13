package io.kk.bandhub.musicservice.repository.genre;

import io.kk.bandhub.musicservice.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGenreRepository extends JpaRepository<Genre, Integer>, GenreRepository {

}
