package io.kk.bandhub.venueservice.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public interface OfferTimeValidatable {
    LocalDate getDate();
    LocalTime getStartTime();
    LocalTime getEndTime();
}
