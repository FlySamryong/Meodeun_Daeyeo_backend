package samryong.global.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import samryong.domain.bank.nonghyup.exception.NonghyupException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e) {

        String errorMessage =
                e.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .findFirst()
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode._INTERNAL_SERVER_ERROR));

        String message = GlobalErrorCode.valueOf(errorMessage).toString();
        return handleExceptionInternal(message);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(GlobalErrorCode._INVALID_PARAMETER);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(GlobalErrorCode globalErrorCode) {
        return ResponseEntity.status(globalErrorCode.getHttpStatus().value())
                .body(new ErrorResponse(globalErrorCode));
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(NonghyupException e) {
        return ResponseEntity.status(e.getHttpStatus().value())
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(String message) {
        return ResponseEntity.status(GlobalErrorCode.valueOf(message).getHttpStatus().value())
                .body(new ErrorResponse(GlobalErrorCode.valueOf(message)));
    }
}
