package dailymissionproject.demo.domain.mission.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionSaveResponseDto {
    private String credential;

    @Builder
    MissionSaveResponseDto(String credential){
        this.credential = credential;
    }
}
