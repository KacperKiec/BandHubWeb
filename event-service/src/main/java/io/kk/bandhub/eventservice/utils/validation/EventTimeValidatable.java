package io.kk.bandhub.eventservice.utils.validation;

import java.time.LocalTime;

public interface EventTimeValidatable {
    LocalTime getStartTime();
    LocalTime getEndTime();
}
