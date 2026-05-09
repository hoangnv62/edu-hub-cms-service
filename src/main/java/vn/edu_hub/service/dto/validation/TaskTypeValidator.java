package vn.edu_hub.service.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import vn.edu_hub.service.constants.TaskTypeEnum;

public class TaskTypeValidator implements ConstraintValidator<TaskType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return true;
        return TaskTypeEnum.find(value) != null;
    }
}
