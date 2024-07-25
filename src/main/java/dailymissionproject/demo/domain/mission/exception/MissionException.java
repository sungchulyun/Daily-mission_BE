package dailymissionproject.demo.domain.mission.exception;

import lombok.Getter;

@Getter
public class MissionException extends RuntimeException {

    private MissionExceptionCode missionExceptionCode;

    public MissionException(MissionExceptionCode missionExceptionCode){
        this.missionExceptionCode = missionExceptionCode;
    }


}
