package dev.nhason.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {UniqueHotelNameValidator.class}
)
public @interface UniqueHotelDetails{
    String message() default "hotel must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
