package samryong.security.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import samryong.domain.member.entity.Member;
import samryong.domain.member.service.MemberService;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;
import samryong.security.resolver.annotation.AuthMember;

@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class)
                && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        Object principal = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication.getName().equals("anonymousUser")) {
                throw new GlobalException(GlobalErrorCode._UNAUTHORIZED);
            } else {
                principal = authentication.getPrincipal();
            }
        }

        if (principal == null || principal.getClass() == String.class) {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) authentication;

        return memberService.getMember(Long.valueOf(usernamePasswordAuthenticationToken.getName()));
    }
}
