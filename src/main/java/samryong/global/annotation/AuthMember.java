package samryong.global.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 해당 사용자가 로그인한 사용자인지 확인하는 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Parameter(hidden = true)
public @interface AuthMember {}
