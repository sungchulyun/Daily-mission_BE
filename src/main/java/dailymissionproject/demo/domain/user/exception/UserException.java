package dailymissionproject.demo.domain.user.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private UserExceptionCode exceptionCode;

    public UserException(UserExceptionCode exceptionCode){
        this.exceptionCode = exceptionCode;
    }


}
