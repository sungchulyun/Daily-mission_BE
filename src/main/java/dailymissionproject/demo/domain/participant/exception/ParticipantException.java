package dailymissionproject.demo.domain.participant.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;
import lombok.Getter;

@Getter
public class ParticipantException extends AbstractCustomException {

    private ParticipantExceptionCode participantExceptionCode;

    public ParticipantException(ParticipantExceptionCode code) {
        super(code);
    }

}
