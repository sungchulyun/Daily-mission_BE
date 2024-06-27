package dailymissionproject.demo.domain.mission.dto.request;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class MissionSaveRequestDto {

    private final Week week;
    private final String title;
    private final String content;
    //private MultipartFile file;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    private MissionSaveRequestDto(Week week, String title, String content
                                , LocalDate startDate, LocalDate endDate){
        this.week = week;
        this.title = title;
        this.content = content;
       // this.file = file;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Mission toEntity(User user){
        MissionRule missionRule = MissionRule.builder().week(week).build();

        return Mission.builder()
                .user(user)
                .missionRule(missionRule)
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
