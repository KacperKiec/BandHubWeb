package io.kk.bandhub.venueservice.utils.validation;

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

        valid = ValidUtils.isTimeValid(dto, context, valid);

        return valid;
    }
}

