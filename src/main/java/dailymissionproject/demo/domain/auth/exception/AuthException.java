package dailymissionproject.demo.domain.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private AuthExceptionCode errorCode;

    public AuthException(AuthExceptionCode errorCode){
        super(errorCode.getMessage());
    }

}
