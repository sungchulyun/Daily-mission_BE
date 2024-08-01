package dailymissionproject.demo.domain.user.exception;


import dailymissionproject.demo.common.exception.AbstractCustomException;

public class UserException extends AbstractCustomException {

    private UserExceptionCode exceptionCode;

    public UserException(UserExceptionCode code){
        super(code);
    }


}
