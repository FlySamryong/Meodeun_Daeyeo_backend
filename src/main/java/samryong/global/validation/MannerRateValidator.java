package samryong.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import samryong.global.annotation.MannerRateValid;
import samryong.global.code.GlobalErrorCode;

@Component
public class MannerRateValidator implements ConstraintValidator<MannerRateValid, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        boolean isValid = value >= 0 && value <= 5;
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(GlobalErrorCode.MANNER_RATE_INVALID.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
