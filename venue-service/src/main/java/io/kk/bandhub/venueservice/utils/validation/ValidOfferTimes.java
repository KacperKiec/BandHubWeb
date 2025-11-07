package io.kk.bandhub.venueservice.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OfferTimesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOfferTimes {
    String message() default "Nieprawidłowa data lub godziny oferty.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
