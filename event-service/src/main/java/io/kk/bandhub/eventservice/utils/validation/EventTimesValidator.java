package io.kk.bandhub.eventservice.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventTimesValidator implements ConstraintValidator<ValidEventTimes, EventTimeValidatable> {

    @Override
    public boolean isValid(EventTimeValidatable dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        return ValidUtils.isTimeValid(dto, context, true);
    }

}
