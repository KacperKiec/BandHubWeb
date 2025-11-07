package io.kk.bandhub.venueservice.utils.validation;

import java.time.LocalDate;

public interface OfferTimeValidatable extends TimeValidatable {
    LocalDate getDate();
}
