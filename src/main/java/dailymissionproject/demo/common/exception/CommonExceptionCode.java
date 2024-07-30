package dailymissionproject.demo.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),

    NOT_SUPPORTED_HTTP_METHOD(HttpStatus.BAD_REQUEST, "지원하지 않는 Http Method 방식입니다."),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 RequestBody 혹은 Argument입니다.");

    private final HttpStatus status;
    private final String message;
}
