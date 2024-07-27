package dailymissionproject.demo.domain.participant.exception;

import lombok.Getter;

@Getter
public class ParticipantException extends RuntimeException {

    private ParticipantExceptionCode participantExceptionCode;

    public ParticipantException(ParticipantExceptionCode participantExceptionCode){
        this.participantExceptionCode = participantExceptionCode;
    }


}
