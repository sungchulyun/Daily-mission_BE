package dailymissionproject.demo.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private CommonExceptionCode commonExceptionCode;

    public CommonException(CommonExceptionCode commonExceptionCode){
        this.commonExceptionCode = commonExceptionCode;
    }


}
