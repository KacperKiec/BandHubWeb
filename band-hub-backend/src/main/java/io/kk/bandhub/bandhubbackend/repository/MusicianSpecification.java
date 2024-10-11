package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.model.Musician;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Specification class provides complex queries
 * */
public class MusicianSpecification {

    public static Specification<Musician> hasCities(List<String> cities) {
        return (root, query, builder) -> root.get("city").in(cities);
    }

    public static Specification<Musician> hasGenres(List<MusicalGenre> genres) {
        return (root, query, builder) -> {
            Join<Object, Object> genreJoin = root.join("genres", JoinType.INNER);
            return genreJoin.in(genres);
        };
    }

    public static Specification<Musician> hasInstruments(List<Instrument> instruments) {
        return (root, query, builder) -> {
            Join<Object, Object> instrumentJoin = root.join("instruments", JoinType.INNER);
            return instrumentJoin.in(instruments);
        };
    }

    public static Specification<Musician> hasSex(String sex) {
        return (root, query, builder) -> builder.equal(root.get("sex"), sex);
    }

    public static Specification<Musician> isLookingForBand(boolean isLookingForBand) {
        return (root, query, builder) -> builder.equal(root.get("isLookingBand"), isLookingForBand);
    }

    public static Specification<Musician> hasName(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }

}
