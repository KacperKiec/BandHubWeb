package io.kk.bandhub.venueservice.repository.offer;

import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.utils.OfferStatus;
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

    public static Specification<Offer> hasCoordinates(Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude) {
        return (root, query, builder) -> {
            if (minLatitude == null || maxLatitude == null || minLongitude == null || maxLongitude == null) {
                return builder.conjunction();
            }

            return builder.and(
                    builder.between(root.get("venue").get("latitude"), minLatitude, maxLatitude),
                    builder.between(root.get("venue").get("longitude"), minLongitude, maxLongitude)
            );
        };
    }
}
