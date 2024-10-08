package samryong.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import samryong.global.validation.MannerRateValidator;

// 매너 온도의 범위를 검증하는 어노테이션
@Documented
@Constraint(validatedBy = MannerRateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MannerRateValid {

    String message() default "매너 온도의 범위는 0-100 사이여야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
