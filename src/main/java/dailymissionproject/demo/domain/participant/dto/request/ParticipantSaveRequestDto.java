package dailymissionproject.demo.domain.participant.dto.request;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.user.repository.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "미션 참여 요청 DTO")
public class ParticipantSaveRequestDto {

    @Schema(description = "참여하고자 하는 미션")
    private final Mission mission;

    @Schema(description = "참여하고자 하는 미션의 참여코드")
    private final String credential;

    @Builder
    public ParticipantSaveRequestDto(Mission mission, String credential){
        this.mission = mission;
        this.credential = credential;
    }

    public Participant toEntity(User user){
        return Participant.builder()
                .mission(mission)
                .user(user)
                .build();
    }
}
