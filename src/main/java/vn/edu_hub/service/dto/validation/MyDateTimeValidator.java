package vn.edu_hub.service.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import vn.edu_hub.service.utils.DateTimeUtils;

import java.time.format.DateTimeParseException;

public class MyDateTimeValidator implements ConstraintValidator<MyDateTime, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return true;
        try {
            DateTimeUtils.dateTimeFormatter.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
