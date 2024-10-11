package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Specification class provides complex queries
 * */
public class BandSpecification {

    public static Specification<Band> hasName(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }

    public static Specification<Band> hasFounderId(int founderId) {
        return (root, query, builder) -> builder.equal(root.get("founderId"), founderId);
    }

    public static Specification<Band> hasGenres(List<MusicalGenre> genres) {
        return (root, query, builder) -> {
            Join<Object, Object> genreJoin = root.join("genres", JoinType.INNER);
            return genreJoin.in(genres);
        };
    }

    public static Specification<Band> hasCity(List<String> cities) {
        return (root, query, builder) -> builder.equal(root.get("city"), cities);
    }

    public static Specification<Band> hasRequestedInstruments(List<Instrument> instruments) {
        return (root, query, builder) -> {
            Join<Object, Object> instrumentJoin = root.join("requestedInstrument", JoinType.INNER);
            return instrumentJoin.in(instruments);
        };
    }

    public static Specification<Band> hasInstruments(List<Instrument> instruments) {
        return (root, query, builder) -> {
            Join<Object, Object> instrumentJoin = root.join("currentInstrument", JoinType.INNER);
            return instrumentJoin.in(instruments);
        };
    }

}
