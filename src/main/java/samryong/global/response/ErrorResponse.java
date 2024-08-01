package samryong.global.response;

import java.time.LocalDateTime;
import lombok.Getter;
import samryong.global.code.GlobalErrorCode;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final Boolean isSuccess;
    private final String code;
    private final String message;

    public ErrorResponse(GlobalErrorCode globalErrorCode) {
        this.isSuccess = false;
        this.code = globalErrorCode.getCode();
        this.message = globalErrorCode.getMessage();
    }
}
