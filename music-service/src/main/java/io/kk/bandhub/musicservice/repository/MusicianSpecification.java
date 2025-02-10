package io.kk.bandhub.musicservice.repository;

import io.kk.bandhub.musicservice.model.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Specification class provides complex queries
 * */
public class MusicianSpecification {

    public static Specification<Musician> hasCities(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("city").in(cities);
        };
    }

    public static Specification<Musician> hasGenres(List<Genre> genres) {
        return (root, query, builder) -> {
            if (genres == null || genres.isEmpty()) {
                return builder.conjunction();
            }

            Join<Musician, MusicianGenre> musicianGenreJoin = root.join("musicianGenres");
            Join<MusicianGenre, Genre> genreJoin = musicianGenreJoin.join("genre");

            return genreJoin.get("id").in(genres.stream().map(Genre::getId).toList());
        };
    }

    public static Specification<Musician> hasInstruments(List<Instrument> instruments) {
        return (root, query, builder) -> {
            if(instruments == null || instruments.isEmpty()) {
                return builder.conjunction();
            }

            Join<Musician, MusicianInstrument> musicianInstrumentJoin = root.join("musicianInstruments");
            Join<MusicianInstrument, Instrument> instrumentJoin = musicianInstrumentJoin.join("instrument");

            return instrumentJoin.get("id").in(instruments.stream().map(Instrument::getId).toList());
        };
    }

    public static Specification<Musician> isLookingForBand(Boolean isLookingForBand) {
        return (root, query, builder) -> {
            if(isLookingForBand == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("isLookingBand"), isLookingForBand);
        };
    }

}
