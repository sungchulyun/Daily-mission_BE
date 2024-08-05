package dailymissionproject.demo.common.exception.handler;


import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.exception.AbstractCustomException;
import dailymissionproject.demo.common.meta.MetaService;
import dailymissionproject.demo.domain.auth.exception.AuthExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;
import java.util.Objects;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String KEY_MESSAGE = "message";

    private ResponseEntity<GlobalResponse> createResponseEntity(Exception exception, HttpStatus status, Map<String, String> message){
        log.warn("예외 발생 : ", exception);
        return new ResponseEntity<>(GlobalResponse.error(status.value(), message), status);
    }

    /**
     * 사용자 정의 예외에 대한 처리
     *
     * @param exception the exception
     * @return the response entity
     */

    @ExceptionHandler(AbstractCustomException.class)
    public ResponseEntity<GlobalResponse> handleCustomException(AbstractCustomException exception){
        String errorMessage = exception.getMessage();
        Map<String, String> message = Map.of(KEY_MESSAGE, errorMessage);
        return createResponseEntity(exception, HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 하위 타입에 속하지 않은 모든 예외에 대한 처리
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<GlobalResponse> handleGenericException(Exception exception){
        Map<String, String> message = Map.of(KEY_MESSAGE, exception.getMessage());
        return createResponseEntity(exception, HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 파라미터 타입이 일치하지 않는 경우에 대한 처리
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalResponse> handleTypeMisMatchException(MethodArgumentTypeMismatchException exception){
        String fieldName = exception.getName();
        String rejectedValue = Objects.requireNonNull(exception.getValue()).toString();
        String requiredType = Objects.requireNonNull(exception.getRequiredType()).getSimpleName();

        String errorMessage = String.format("'%s' 필드는 '%s' 타입이 필요하지만, 잘못된 값 '%s'가 입력되었습니다.",
                fieldName, rejectedValue, requiredType);

        Map<String, String> message = Map.of(KEY_MESSAGE, errorMessage);
        return new ResponseEntity<>(GlobalResponse.error(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SignatureException.class, MalformedJwtException.class, ExpiredJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<GlobalResponse> jwtTokenException(Exception e){

        AuthExceptionCode exceptionType = getJwtExceptionType(e);

        GlobalResponse fail = GlobalResponse.fail(
                exceptionType.getHttpStatus().value(),
                exceptionType.getMessage(),
                MetaService.createMetaInfo().add("login-url", "/login")
        );

        return ResponseEntity
                .status(exceptionType.getHttpStatus())
                .body(fail);
    }

    private AuthExceptionCode getJwtExceptionType(Exception e){
        if(e instanceof SignatureException){
            return AuthExceptionCode.INVALID_SIGNATURE;
        } else if(e instanceof MalformedJwtException){
            return AuthExceptionCode.MALFORMED_TOKEN;
        } else if(e instanceof ExpiredJwtException){
            return AuthExceptionCode.EXPIRE_TOKEN;
        } else if(e instanceof IllegalArgumentException){
            return AuthExceptionCode.ILLEGAL_ARGUMENT;
        } else {
            return AuthExceptionCode.UNKNOWN_ERROR;
        }
    }

}
