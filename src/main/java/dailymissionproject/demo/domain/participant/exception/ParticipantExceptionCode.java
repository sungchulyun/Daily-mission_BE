package dailymissionproject.demo.domain.participant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ParticipantExceptionCode {

    SUCCESS(HttpStatus.OK, "OK"),
    BAN_HISTORY_EXISTS(HttpStatus.BAD_REQUEST, "강퇴당한 미션에는 재참여가 불가능합니다."),
    PARTICIPANT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 참여중인 미션입니다."),
    INVALID_INPUT_VALUE_CREDENTIAL(HttpStatus.BAD_REQUEST, "잘못된 참여코드입니다."),
    INVALID_PARTICIPATE_REQUEST(HttpStatus.BAD_REQUEST, "해당 미션은 참여가 불가능한 미션입니다.");


    private final HttpStatus status;
    private final String message;
}
