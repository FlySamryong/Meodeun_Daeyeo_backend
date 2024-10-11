package samryong.domain.redis.service;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import samryong.domain.member.entity.Member;
import samryong.domain.member.repository.MemberRepository;
import samryong.domain.member.service.MemberService;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final long EXPIRATION_TIME_IN_MILLIS = 1000 * 60 * 60 * 12; // 12시간

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60) // 1시간 마다 실행
    @Transactional
    public void checkMannerRateExpiration() {
        Set<String> keys = redisTemplate.keys("MANNER_RATE*");
        if (keys == null || keys.isEmpty()) return; // null 또는 빈 경우 빠르게 반환

        keys.forEach(
                key -> {
                    Long mannerRate = (Long) redisTemplate.opsForHash().get(key, "mannerRate");
                    Long createdAt = (Long) redisTemplate.opsForHash().get(key, "createdAt");

                    // 만료 확인
                    if (mannerRate != null && isKeyExpired(createdAt)) {
                        // mannerRate가 존재하고 만료된 경우에만 업데이트 및 삭제
                        memberService.updateMannerRate(extractMemberFromKey(key), mannerRate);
                        redisTemplate.delete(key); // 만료된 키는 삭제
                    } else if (mannerRate == null) {
                        redisTemplate.delete(key);
                    }
                });
    }

    private boolean isKeyExpired(Long createdAt) {
        if (createdAt == null) return false;

        long currentTime = System.currentTimeMillis();
        return currentTime - createdAt >= EXPIRATION_TIME_IN_MILLIS;
    }

    private Member extractMemberFromKey(String key) {
        Long memberId = Long.parseLong(key.split(":")[1]); // 키에서 memberId 추출

        return memberRepository
                .findById(memberId)
                .orElseThrow(
                        () -> new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND)); // memberId로 DB에서 조회
    }
}
