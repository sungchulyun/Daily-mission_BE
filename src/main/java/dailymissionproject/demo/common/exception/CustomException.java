package dailymissionproject.demo.common.exception;

import dailymissionproject.demo.common.exception.code.ExceptionCode;
import lombok.Getter;

@Getter
public class CustomException extends AbstractCustomException{
    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
