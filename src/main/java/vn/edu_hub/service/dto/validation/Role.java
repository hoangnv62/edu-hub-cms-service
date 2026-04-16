package vn.edu_hub.service.dto.validation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {RoleValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface Role {
    String message() default "Role không hợp lệ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
