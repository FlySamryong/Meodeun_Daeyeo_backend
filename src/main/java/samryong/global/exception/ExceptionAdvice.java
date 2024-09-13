package samryong.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import samryong.bank.nonghyup.exception.NonghyupException;
import samryong.global.code.GlobalErrorCode;
import samryong.global.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e) {
        log.error("{}: {}", e.getGlobalErrorCode(), e.getMessage());
        return handleExceptionInternal(e.getGlobalErrorCode());
    }

    @ExceptionHandler(NonghyupException.class)
    public ResponseEntity<ErrorResponse> handleNonghyupException(NonghyupException e) {
        log.error("{}: {}", e.getCode(), e.getMessage());
        return handleExceptionInternal(e);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(GlobalErrorCode globalErrorCode) {
        return ResponseEntity.status(globalErrorCode.getHttpStatus().value())
                .body(new ErrorResponse(globalErrorCode));
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(NonghyupException e) {
        return ResponseEntity.status(e.getHttpStatus().value())
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }
}
