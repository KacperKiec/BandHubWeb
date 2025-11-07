package io.kk.bandhub.venueservice.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookingTimesValidator implements ConstraintValidator<ValidBookingTimes, BookingTimeValidatable> {

    @Override
    public boolean isValid(BookingTimeValidatable dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        return ValidUtils.isTimeValid(dto, context, true);
    }

}
