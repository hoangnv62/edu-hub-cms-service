package vn.edu_hub.service.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import vn.edu_hub.service.constants.Authorities;

public class RoleValidator implements ConstraintValidator<Role, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        return Authorities.find(s) != null;
    }
}
