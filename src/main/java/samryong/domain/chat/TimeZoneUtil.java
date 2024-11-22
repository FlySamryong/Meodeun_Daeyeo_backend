package samryong.domain.chat;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimeZoneUtil {

    // UTC -> KST 변환 (9시간 더하기)
    public static LocalDateTime toPlus9Hours(LocalDateTime utcTime) {
        return utcTime.plusHours(9);
    }

    // KST -> UTC 변환 (9시간 빼기)
    public static LocalDateTime toMinus9Hours(LocalDateTime kstTime) {
        return kstTime.minusHours(9);
    }
}
