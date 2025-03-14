package com.project4.restaurant.domain.core.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidNumericLength.NumericLengthValidator.class)  // Chỉ định validator sẽ thực hiện kiểm tra
@Target({ElementType.FIELD, ElementType.PARAMETER})     // Đặt lên trường hoặc tham số
@Retention(RetentionPolicy.RUNTIME)                      // Giữ lại trong runtime
public @interface ValidNumericLength {

  String message() default "Số vượt quá độ dài cho phép";  // Thông báo lỗi mặc định

  int maxLength();  // Độ dài tối đa

  Class<?>[] groups() default {}; // Cần thiết cho Hibernate Validator

  Class<? extends Payload>[] payload() default {}; // Payload tùy chỉnh, cũng cần thiết cho Hibernate Validator

  class NumericLengthValidator implements ConstraintValidator<ValidNumericLength, Number> {

    private int maxLength;

    @Override
    public void initialize(ValidNumericLength constraintAnnotation) {
      this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;  // Cho phép giá trị null (nếu cần kiểm tra null thì thêm annotation khác)
      }

      // Chuyển số thành chuỗi để tính độ dài
      String numericString = String.valueOf(value).replaceAll("\\.0+$", "").replaceAll("[.,]", "");

      // Kiểm tra độ dài của số
      return numericString.length() <= maxLength;
    }
  }
}
