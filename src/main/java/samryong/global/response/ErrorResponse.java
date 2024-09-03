package samryong.global.response;

import lombok.Getter;
import samryong.global.code.GlobalErrorCode;

@Getter
public class ErrorResponse {

    private final Boolean isSuccess;
    private final String code;
    private final String message;

    public ErrorResponse(GlobalErrorCode globalErrorCode) {
        this.isSuccess = false;
        this.code = globalErrorCode.getCode();
        this.message = globalErrorCode.getMessage();
    }
}
