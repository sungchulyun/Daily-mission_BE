package dailymissionproject.demo.domain.participant.dto.request;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantSaveRequestDto {

    private final Mission mission;
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
