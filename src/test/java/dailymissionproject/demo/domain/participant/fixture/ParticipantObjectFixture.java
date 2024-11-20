package dailymissionproject.demo.domain.participant.fixture;

import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;

public class ParticipantObjectFixture {

    public static ParticipantSaveRequestDto getParticipantSaveRequest(){
        return ParticipantSaveRequestDto.builder()
                .credential("CREDENTIAL")
                .missionId(2L)
                .build();
    }
}
