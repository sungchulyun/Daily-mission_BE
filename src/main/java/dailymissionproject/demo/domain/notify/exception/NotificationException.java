package dailymissionproject.demo.domain.notify.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;
import dailymissionproject.demo.common.exception.code.ExceptionCode;

public class NotificationException extends AbstractCustomException {
    private NotificationExceptionCode exceptionCode;

    public NotificationException(ExceptionCode code) {
        super(code);
    }

}
