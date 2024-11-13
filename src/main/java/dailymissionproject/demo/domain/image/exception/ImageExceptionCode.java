package dailymissionproject.demo.domain.image.exception;

import dailymissionproject.demo.common.exception.code.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum ImageExceptionCode implements ExceptionCode {
    SUCCESS(HttpStatus.OK, "OK"),
    INPUT_VALUE_IS_EMPTY(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    INPUT_VALUE_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "한글, 영어, 숫자, 하이픈, 밑줄만 가능합니다."),
    INPUT_VALUE_LENGTH_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "입력값의 길이가 큽니다. 100이하만 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ImageExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
