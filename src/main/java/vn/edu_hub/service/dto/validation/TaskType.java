package vn.edu_hub.service.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {TaskTypeValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface TaskType {
    String message() default "Loại bài tập không hợp lệ, các giá trị hợp lệ: HOMEWORK, EXAM, TEST";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
