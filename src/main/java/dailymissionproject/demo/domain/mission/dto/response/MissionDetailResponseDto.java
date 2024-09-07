package dailymissionproject.demo.domain.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Schema(description = "미션 상세 정보 응답 DTO")
public class MissionDetailResponseDto {

    @Schema(description = "미션 제목")
    private final String title;
    @Schema(description = "미션 내용")
    private final String content;
    @Schema(description = "미션 썸네일 이미지")
    private final String imageUrl;
    @Schema(description = "방장 닉네임")
    private final String nickname;
    @Schema(description = "미션 힌트")
    private final String hint;

    @Schema(description = "참여자들 목록")
    private List<ParticipantUserDto> participantDto = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "미션 시작일자")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private final LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "미션 종료일자")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private final LocalDate endDate;

    @Schema(description = "미션 규칙")
    private final MissionRuleResponseDto missionRuleResponseDto;


    public static MissionDetailResponseDto of(Mission mission){
        return new MissionDetailResponseDto(
                mission.getTitle(),
                mission.getContent(),
                mission.getImageUrl(),
                mission.getUser().getNickname(),
                mission.getHint(),
                mission.getStartDate(),
                mission.getEndDate(),
                MissionRuleResponseDto.of(mission.getMissionRule())
        );
    }

    @Builder
    public MissionDetailResponseDto(String title, String content, String imageUrl, String nickname, String hint, List<ParticipantUserDto> participantUserDto
            , LocalDate startDate, LocalDate endDate, MissionRuleResponseDto missionRuleResponseDto ) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.hint = hint;
        this.participantDto = participantUserDto;;
        this.startDate = startDate;
        this.endDate = endDate;
        this.missionRuleResponseDto = missionRuleResponseDto;
    }

    public void setParticipants(List<ParticipantUserDto> participants) {
        this.participantDto = participants;
    }
}
