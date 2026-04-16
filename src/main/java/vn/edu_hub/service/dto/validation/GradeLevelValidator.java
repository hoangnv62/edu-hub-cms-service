package vn.edu_hub.service.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import vn.edu_hub.service.constants.GradeLeverEnum;


public class GradeLevelValidator implements ConstraintValidator<GradeLevel, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        return GradeLeverEnum.find(s) != null;
    }
}
