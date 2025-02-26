package io.kk.bandhub.venueservice.repository.offer;

import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.OfferStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class OfferSpecification {

    public static Specification<Offer> hasCity(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return builder.isTrue(root.get("venue").get("city").in(cities));
        };
    }

    public static Specification<Offer> hasStatus(OfferStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<Offer> hasDate(LocalDate date) {
        return (root, query, builder) -> {
            if (date == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("date"), date);
        };
    }
}
