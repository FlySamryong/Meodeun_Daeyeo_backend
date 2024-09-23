package samryong.global.interceptor;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;
import samryong.security.provider.JwtTokenProvider;
import samryong.security.userDetail.MemberDetailsService;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = extractToken(accessor.getNativeHeader("Authorization"));
            if (jwtTokenProvider.validateToken(token)) {
                authenticateUser(token);
            } else {
                throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
            }
        }

        return message;
    }

    private String extractToken(List<String> authorization) {
        if (authorization == null || authorization.isEmpty()) {
            throw new GlobalException(GlobalErrorCode._UNAUTHORIZED);
        }
        return authorization.get(0).substring(7); // Assuming Bearer scheme
    }

    private void authenticateUser(String token) {
        Long memberId = jwtTokenProvider.getMemberId(token);
        UserDetails userDetails = memberDetailsService.loadUserByUsername(memberId.toString());

        if (userDetails == null) {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
