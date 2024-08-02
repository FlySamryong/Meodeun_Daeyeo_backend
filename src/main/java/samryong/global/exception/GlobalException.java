package samryong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import samryong.global.code.GlobalErrorCode;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException {

    private GlobalErrorCode globalErrorCode;
}
