package dailymissionproject.demo.domain.mission.dto.response;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MissionResponseDto {

    private String title;
    private String content;
    private String imgUrl;
    //방장 아이디
    private Long userId;
    private String userName;

    private List<ParticipantUserDto> participants = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public MissionResponseDto(Mission mission){
        this.title = mission.getTitle();
        this.content = mission.getContent();
        this.imgUrl = mission.getImageUrl();
        this.userId = mission.getUser().getId();
        this.userName = mission.getUser().getName();
        this.participants = mission.getAllParticipantUser();
        this.startDate = mission.getStartDate();
        this.endDate = mission.getEndDate();
    }
}
