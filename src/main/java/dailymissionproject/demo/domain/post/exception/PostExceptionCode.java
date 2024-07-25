package dailymissionproject.demo.domain.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),

    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 포스트가 삭제되었거나 존재하지 않습니다."),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 RequestBody 혹은 Argument입니다.");

    private final HttpStatus status;
    private final String message;
}
