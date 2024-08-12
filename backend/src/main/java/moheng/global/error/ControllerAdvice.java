package moheng.global.error;

import moheng.auth.exception.*;
import moheng.global.error.dto.ExceptionResponse;
import moheng.liveinformation.exception.EmptyLiveInformationException;
import moheng.member.exception.*;
import org.apache.coyote.Response;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler({
            BadRequestException.class,
            NoExistOAuthClientException.class,
            NoExistOAuthClientException.class,
            InvalidBirthdayException.class,
            InvalidEmailFormatException.class,
            InvalidGenderFormatException.class,
            InvalidNicknameFormatException.class,
            NoExistMemberTokenException.class,
            NoExistMemberException.class,
            NoExistSocialTypeException.class,
            EmptyLiveInformationException.class,
            ShortContentidsSizeException.class
    })
    public ResponseEntity<ExceptionResponse> handleIBadRequestException(final RuntimeException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }


    @ExceptionHandler(InvalidOAuthServiceException.class)
    public ResponseEntity<ExceptionResponse> handleOAuthException(final RuntimeException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler({
            EmptyBearerHeaderException.class,
            InvalidTokenFormatException.class,
            InvalidTokenException.class,
            DuplicateNicknameException.class
    })
    public ResponseEntity<ExceptionResponse> handleUnAuthorizedException(final RuntimeException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse errorResponse = new ExceptionResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequestBody() {
        ExceptionResponse exceptionResponse = new ExceptionResponse("잘못된 Request Body 형식 입니다.");
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleNotSupportedMethod() {
        ExceptionResponse errorResponse = new ExceptionResponse("잘못된 HTTP 메소드 요청입니다.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatch() {
        ExceptionResponse exceptionResponse = new ExceptionResponse("잘못된 타입을 가진 데이터가 포함되어 있습니다.");
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleOverflowException(final Exception e) {
        logger.error(e.getMessage(), e);

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse("서버에 예기치 못한 오류가 발생했습니다. 관리자에게 문의하세요."));
    }
}
