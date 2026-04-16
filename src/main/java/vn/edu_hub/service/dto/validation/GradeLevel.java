package vn.edu_hub.service.dto.validation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {GradeLevelValidator.class})
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface GradeLevel {
    String message() default "Invalid Grade Level";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
