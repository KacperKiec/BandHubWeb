package io.kk.bandhub.eventservice.repository;

import io.kk.bandhub.eventservice.model.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EventSpecification {

    public static Specification<Event> hasBandId(Integer bandId) {
        return (root, query, builder) -> {
            if (bandId == null || bandId < 0) {
                return builder.conjunction();
            }
            return builder.equal(root.get("bandId"), bandId);
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
}
