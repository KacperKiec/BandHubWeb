package io.kk.bandhub.eventservice.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventTimesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventTimes {
    String message() default "Nieprawidłowa data lub godziny wydarzenia.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
