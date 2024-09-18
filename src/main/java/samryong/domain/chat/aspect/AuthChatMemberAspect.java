package samryong.domain.chat.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import samryong.domain.chat.service.ChatRoomService;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthChatMemberAspect {

    private final ChatRoomService chatRoomService;

    @Before("@annotation(samryong.domain.chat.aspect.annotation.AuthChatMember) && args(roomId)")
    public void authChatMember(Long roomId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) authentication;

        Long memberId = Long.valueOf(usernamePasswordAuthenticationToken.getName());

        // 채팅방에 속한 사용자 검증
        boolean isMember = chatRoomService.isMemberOfChatRoom(roomId, memberId);

        if (!isMember) {
            throw new GlobalException(GlobalErrorCode._FORBIDDEN);
        }
    }
}
