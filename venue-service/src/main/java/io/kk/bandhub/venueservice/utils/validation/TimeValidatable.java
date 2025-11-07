package io.kk.bandhub.venueservice.utils.validation;

import java.time.LocalTime;

public interface TimeValidatable {
    LocalTime getStartTime();
    LocalTime getEndTime();
}
