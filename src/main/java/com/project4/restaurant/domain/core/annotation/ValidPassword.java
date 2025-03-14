package com.project4.restaurant.domain.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Constraint(validatedBy = ValidPassword.PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
  String message() default "Vui lòng nhập đúng định dạng mật khẩu";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final Pattern MIDDLE_WHITESPACE_PATTERN = Pattern.compile("\\S+\\s+\\S+");
    private static final Pattern VIETNAMESE_DIACRITIC_PATTERN = Pattern.compile
        ("[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]", Pattern.CASE_INSENSITIVE);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      if (!value.equals(value.trim())) {
        return false;
      }
      if (MIDDLE_WHITESPACE_PATTERN.matcher(value).find()) {
        return false;
      }
      return !VIETNAMESE_DIACRITIC_PATTERN.matcher(value).find();
    }
  }
}
