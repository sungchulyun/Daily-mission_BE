package dailymissionproject.demo.domain.like.exception;

import dailymissionproject.demo.common.exception.code.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum LikesExceptionCode implements ExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),
    INVALID_LIKE_REQUEST(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 포스트입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    LikesExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
