package com.project4.restaurant.domain.core.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
@Constraint(validatedBy = NoSpecialCharacters.NoSpecialCharactersValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpecialCharacters {
  String message() default "Không được chưa ký tự đặc biệt";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NoSpecialCharactersValidator implements ConstraintValidator<NoSpecialCharacters, String> {
    //    private static final String SPECIAL_CHARACTERS_REGEX = "^[\\p{L}\\d\\s]+$";
    private static final String SPECIAL_CHARACTERS_REGEX = "^[^!@#$%^&*()_+={}\\\\[\\\\]|:;\\\"'<>.,?/~-]+$";

    @Override
    public void initialize(NoSpecialCharacters constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null || value.isEmpty()) {
        return true;
      }
      return value.replaceAll(" ", "").matches(SPECIAL_CHARACTERS_REGEX);
    }
  }
}
