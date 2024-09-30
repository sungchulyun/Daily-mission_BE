package dailymissionproject.demo.domain.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "미션 수정 요청 DTO")
public class MissionUpdateRequestDto {
    @Schema(description = "미션 참여코드 힌트")
    private final String hint;

    @Schema(description = "미션 참여코드")
    private final String credential;

    @Builder
    public MissionUpdateRequestDto(String hint, String credential) {
        this.hint = hint;
        this.credential = credential;
    }
}
