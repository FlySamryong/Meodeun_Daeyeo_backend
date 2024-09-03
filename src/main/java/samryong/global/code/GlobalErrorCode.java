package samryong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {

    // client 측 오류로 인한 에러(400번대 에러), 서버 측 오류로 인한 에러(500번대 에러)

    // 일반적인 경우의 에러(common)
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 데이터를 찾을 수 없습니다"),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),

    // 사용자 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER400", "해당 사용자를 찾을 수 없습니다."),

    // 인증 관련 에러
    KAKAO_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "AUTH403", "카카오 인증에 실패했습니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH401", "만료된 토큰입니다."),
    AUTH_INVALID_TOKEN(HttpStatus.NOT_FOUND, "AUTH402", "유효하지 않은 토큰입니다.."),
    AUTH_INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH403", "유효하지 않은 리프레시 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
