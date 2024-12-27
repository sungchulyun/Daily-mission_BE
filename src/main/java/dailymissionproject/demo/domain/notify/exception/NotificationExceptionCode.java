package dailymissionproject.demo.domain.notify.exception;

import dailymissionproject.demo.common.exception.code.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum NotificationExceptionCode implements ExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    NotificationExceptionCode(HttpStatus httpStatus, String message) {
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
