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
            return builder.equal(root.get("founderId"), founderId);
        };
    }

    public static Specification<Band> hasGenres(List<MusicalGenre> genres) {
        return (root, query, builder) -> {
            if (genres == null || genres.isEmpty()) {
                return builder.conjunction();
            }
            Join<Object, Object> genreJoin = root.join("genres", JoinType.INNER);
            return genreJoin.in(genres);
        };
    }

    public static Specification<Band> hasCity(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get("city"), cities);
        };
    }

    public static Specification<Band> hasRequestedInstruments(List<Instrument> instruments) {
        return (root, query, builder) -> {
            if (instruments == null || instruments.isEmpty()) {
                return builder.conjunction();
            }
            Join<Object, Object> instrumentJoin = root.join("requestedInstrument", JoinType.INNER);
            return instrumentJoin.in(instruments);
        };
    }

    public static Specification<Band> hasInstruments(List<Instrument> instruments) {
        return (root, query, builder) -> {
            if (instruments == null || instruments.isEmpty()) {
                return builder.conjunction();
            }
            Join<Object, Object> instrumentJoin = root.join("currentInstrument", JoinType.INNER);
            return instrumentJoin.in(instruments);
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
