package dailymissionproject.demo.common.exception.code;

import org.springframework.http.HttpStatus;


public interface ExceptionCode {

   String getMessage();

   HttpStatus getHttpStatus();
}
