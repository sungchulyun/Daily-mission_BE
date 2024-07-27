package dailymissionproject.demo.domain.mission.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MissionExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 미션은 존재하지 않거나 폐기되었습니다."),
    MISSION_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 미션입니다."),
    INVALID_DELETE_REQUEST(HttpStatus.BAD_REQUEST, "다른 사용자가 참여중인 미션을 삭제가 불가능합니다."),
    INVALID_USER_DELETE_REQUEST(HttpStatus.UNAUTHORIZED, "방장만 삭제가 가능합니다."),
    INPUT_VALUE_IS_EMPTY(HttpStatus.BAD_REQUEST, "참여할 미션을 선택하지 않았습니다."),
    INPUT_START_VALUE_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "미션 시작일자는 현재보다 빠를 수 없습니다."),
    INPUT_DELETE_VALUE_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "미션 종료일자는 현재보다 느릴 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
