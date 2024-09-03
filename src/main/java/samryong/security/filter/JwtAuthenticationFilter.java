package samryong.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;
import samryong.security.provider.JwtTokenProvider;
import samryong.security.userDetail.MemberDetailsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveBearerToken(request);

        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                Long memberId = jwtTokenProvider.getMemberId(token);
                UserDetails userDetails = memberDetailsService.loadUserByUsername(memberId.toString());
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, "", userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
                }
            } else {
                throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘어가기
    }
}
