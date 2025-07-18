package io.kk.bandhub.venueservice.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class OfferTimesValidator implements ConstraintValidator<ValidOfferTimes, OfferTimeValidatable> {

    @Override
    public boolean isValid(OfferTimeValidatable dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        boolean valid = true;

        if (dto.getDate() != null && dto.getDate().isBefore(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Date must not be earlier than current date.")
                    .addPropertyNode("date").addConstraintViolation();
            valid = false;
        }

        if (dto.getStartTime() != null && dto.getEndTime() != null &&
                dto.getStartTime().isAfter(dto.getEndTime())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start time must not be after end time.")
                    .addPropertyNode("startTime").addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}

