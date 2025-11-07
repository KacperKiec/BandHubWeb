package io.kk.bandhub.venueservice.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookingTimesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingTimes {
    String message() default "Nieprawidłowa data lub godziny rezerwacji.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
