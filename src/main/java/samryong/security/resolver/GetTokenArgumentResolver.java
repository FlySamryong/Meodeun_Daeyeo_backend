package samryong.security.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import samryong.global.annotation.GetToken;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isString = parameter.getParameterType().equals(String.class);
        boolean isGetToken = parameter.hasParameterAnnotation(GetToken.class);

        return isString && isGetToken;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        String refreshToken = webRequest.getHeader("Authorization");
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new GlobalException(GlobalErrorCode._UNAUTHORIZED);
        }
        if (!refreshToken.startsWith("Bearer ")) {
            throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
        }

        return refreshToken.substring(7);
    }
}
