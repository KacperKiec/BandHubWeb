package io.kk.bandhub.eventservice.repository;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> hasName(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get("name"), name);
        };
    }

    public static Specification<Event> hasOrganizerId(Integer organizerId) {
        return (root, query, builder) -> {
            if (organizerId == null || organizerId < 0) {
                return builder.conjunction();
            }
            return builder.equal(root.get("organizerId"), organizerId);
        };
    }

    public static Specification<Event> hasDate(LocalDate date) {
        return (root, query, builder) -> {
            if (date == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("date"), date);
        };
    }

    public static Specification<Event> hasCities(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return builder.isTrue(root.get("venue").get("city").in(cities));
        };
    }
}
