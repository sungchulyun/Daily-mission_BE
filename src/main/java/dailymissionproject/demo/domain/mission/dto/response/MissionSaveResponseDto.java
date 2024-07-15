package dailymissionproject.demo.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "미션 생성 응답 DTO")
public class MissionSaveResponseDto {

    @Schema(description = "미션 참여를 위한 암호")
    private String credential;

    @Builder
    MissionSaveResponseDto(String credential){
        this.credential = credential;
    }
}
