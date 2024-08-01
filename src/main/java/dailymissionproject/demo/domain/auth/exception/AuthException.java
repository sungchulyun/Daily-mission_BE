package dailymissionproject.demo.domain.auth.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;

public class AuthException extends AbstractCustomException {

    public AuthException(AuthExceptionCode code){
        super(code);
    }

}
