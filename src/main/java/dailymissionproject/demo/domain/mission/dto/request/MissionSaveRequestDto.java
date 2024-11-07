package dailymissionproject.demo.domain.mission.dto.request;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.user.repository.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Schema(description = "미션 생성 요청 DTO")
public class MissionSaveRequestDto {

    @Schema(description = "미션의 포스트 필수 제출 요일")
    private final Week week;

    @Schema(description = "미션 제목")
    private final String title;

    @Schema(description = "미션 내용")
    private final String content;

    @Schema(description = "미션 이미지")
    private final String imageUrl;

    @Schema(description = "미션 참여코드 힌트")
    private final String hint;

    @Schema(description = "미션 참여코드")
    private final String credential;

    @Schema(description = "미션 시작일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "미션 종료일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    private MissionSaveRequestDto(Week week, String title, String content, String imageUrl, String hint, String credential
                                , LocalDate startDate, LocalDate endDate){
        this.week = week;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.hint = hint;
        this.credential = credential;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Mission toEntity(User user){
        MissionRule missionRule = MissionRule.builder()
                                             .week(week)
                                             .build();

        return Mission.builder()
                .user(user)
                .missionRule(missionRule)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .hint(hint)
                .credential(credential)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
