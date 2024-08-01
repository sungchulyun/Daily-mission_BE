package dailymissionproject.demo.domain.mission.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;
import lombok.Getter;

@Getter
public class MissionException extends AbstractCustomException {
    public MissionException(MissionExceptionCode code){
        super(code);
    }


}
