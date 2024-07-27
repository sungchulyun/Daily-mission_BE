package dailymissionproject.demo.domain.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 삭제되었거나 존재하지 않습니다."),
    POST_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 작성한 포스트가 존재합니다."),
    INVALID_POST_SAVE_REQUEST(HttpStatus.BAD_REQUEST, "참여중인 미션에만 인증글을 작성할 수 있습니다.");

    private final HttpStatus status;
    private final String message;
}
