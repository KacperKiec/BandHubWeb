package io.kk.bandhub.venueservice.utils.validation;

import jakarta.validation.ConstraintValidatorContext;

public class ValidUtils {

    public static boolean isTimeValid(TimeValidatable dto, ConstraintValidatorContext context, boolean valid) {
        if (dto.getStartTime() != null && dto.getEndTime() != null &&
                dto.getStartTime().isAfter(dto.getEndTime())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start time must not be after end time.")
                    .addPropertyNode("startTime").addConstraintViolation();
            valid = false;
        }

        if ((dto.getStartTime().getMinute() != 0 && dto.getStartTime().getMinute() != 30)
                || (dto.getEndTime().getMinute() != 0 && dto.getEndTime().getMinute() != 30)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Times must be multiples of 30 minutes")
                    .addPropertyNode("startTime").addPropertyNode("endTime").addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
