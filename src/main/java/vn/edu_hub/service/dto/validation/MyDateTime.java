package vn.edu_hub.service.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {MyDateTimeValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface MyDateTime {
    String message() default "Thời gian không hợp lệ, định dạng yêu cầu: yyyy-MM-dd HH:mm";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
