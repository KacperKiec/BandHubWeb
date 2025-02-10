package io.kk.bandhub.musicservice.repository;

import io.kk.bandhub.musicservice.model.Band;
import io.kk.bandhub.musicservice.model.BandGenre;
import io.kk.bandhub.musicservice.model.Genre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Specification class provides complex queries
 * */
public class BandSpecification {

    public static Specification<Band> hasName(String name) {
        return (root, query, builder) -> {
            if(name == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("name"), name);
        };
    }

    public static Specification<Band> hasFounderId(Integer founderId) {
        return (root, query, builder) -> {
            if (founderId == null) {
                return builder.conjunction();
            }

            Join<Object, Object> bandMusicianJoin = root.join("bandMusicians");

            Predicate musicianPredicate = builder.equal(bandMusicianJoin.get("musician").get("id"), founderId);
            Predicate founderPredicate = builder.isTrue(bandMusicianJoin.get("isFounder"));

            return builder.and(musicianPredicate, founderPredicate);
        };
    }


    public static Specification<Band> hasGenres(List<Genre> genres) {
        return (root, query, builder) -> {
            if (genres == null || genres.isEmpty()) {
                return builder.conjunction();
            }

            Join<Band, BandGenre> bandGenreJoin = root.join("bandGenres");
            Join<BandGenre, Genre> genreJoin = bandGenreJoin.join("genre");

            return genreJoin.get("id").in(genres.stream().map(Genre::getId).toList());
        };
    }

    public static Specification<Band> hasCity(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("city").in(cities);
        };
    }

    public static Specification<Band> isLookingMembers(Boolean isLookingMembers) {
        return (root, query, builder) -> {
            if (isLookingMembers == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("isLookingMembers"), isLookingMembers);
        };
    }

}
