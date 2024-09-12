package dailymissionproject.demo.domain.mission.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class MissionUpdateResponseDto {
    private final String hint;
    private final String credential;

    @Builder
    public MissionUpdateResponseDto(String hint, String credential) {
        this.hint = hint;
        this.credential = credential;
    }
}
