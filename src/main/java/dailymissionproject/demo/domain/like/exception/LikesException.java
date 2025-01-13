package dailymissionproject.demo.domain.like.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;
import dailymissionproject.demo.common.exception.code.ExceptionCode;
import lombok.Getter;

@Getter
public class LikesException extends AbstractCustomException {
    public LikesException(LikesExceptionCode code) {
        super(code);
    }
}
