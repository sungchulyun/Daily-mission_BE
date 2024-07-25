package dailymissionproject.demo.domain.mission.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MissionExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),

    MISSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 미션은 존재하지 않거나 폐기되었습니다."),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 RequestBody 혹은 Argument입니다.");

    private final HttpStatus status;
    private final String message;
}
