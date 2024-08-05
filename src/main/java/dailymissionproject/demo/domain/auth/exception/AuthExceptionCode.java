package dailymissionproject.demo.domain.auth.exception;

import dailymissionproject.demo.common.exception.code.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum AuthExceptionCode implements ExceptionCode {

    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED,"잘못된 JWT 토큰입니다."),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰입니다."),
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "JWT 처리 중 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
    AuthExceptionCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage(){
        return message;
    }
}