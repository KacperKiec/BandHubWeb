package io.kk.bandhub.venueservice.repository.venue;

import io.kk.bandhub.venueservice.model.Venue;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class VenueSpecification {

    public static Specification<Venue> hasName(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get("name"), name);
        };
    }

    public static Specification<Venue> hasCities(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("city").in(cities);
        };
    }

    public static Specification<Venue> hasLessCapacity(Integer capacity) {
        return (root, query, builder) -> {
            if (capacity == null || capacity < 0) {
                return builder.conjunction();
            }
            return builder.lessThanOrEqualTo(root.get("capacity"), capacity);
        };
    }

    public static Specification<Venue> hasMoreCapacity(Integer capacity) {
        return (root, query, builder) -> {
            if (capacity == null || capacity < 0) {
                return builder.conjunction();
            }
            return builder.greaterThanOrEqualTo(root.get("capacity"), capacity);
        };
    }

    public static Specification<Venue> hasOwnerId(Integer ownerId) {
        return (root, query, builder) -> {
            if (ownerId == null || ownerId < 0) {
                return builder.conjunction();
            }
            return builder.equal(root.get("ownerId"), ownerId);
        };
    }
}
