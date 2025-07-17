package io.kk.bandhub.venueservice.repository.booking;

import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookingSpecification {

    public static Specification<Booking> hasOfferId(Integer offerId) {
        return (root, query, builder) -> {
            if (offerId == null || offerId < 0) {
                return builder.conjunction();
            }
            return builder.equal(root.get("offer").get("id"), offerId);
        };
    }

    public static Specification<Booking> hasTenantId(Integer tenantId) {
        return (root, query, builder) -> {
            if (tenantId == null || tenantId < 0) {
                return builder.conjunction();
            }
            return builder.equal(root.get("tenantId"), tenantId);
        };
    }

    public static Specification<Booking> hasStatus(OfferStatus status) {
        return (root, query, builder) -> {
            if(status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }
}
