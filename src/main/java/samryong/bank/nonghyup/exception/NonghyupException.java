package samryong.bank.nonghyup.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class NonghyupException extends RuntimeException {

    // 농협 API 응답으로부터 오는 에러 코드와 메시지를 담는 클래스
    private HttpStatus httpStatus;
    private String code;
    private String message;
}
