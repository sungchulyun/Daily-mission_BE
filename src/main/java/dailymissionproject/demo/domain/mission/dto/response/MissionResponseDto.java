package dailymissionproject.demo.domain.mission.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "미션 상세 정보 응답 DTO")
public class MissionResponseDto {

    @Schema(description = "미션 제목")
    private String title;
    @Schema(description = "미션 내용")
    private String content;
    @Schema(description = "미션 썸네일 이미지")
    private String imgUrl;
    @Schema(description = "방장 이름")
    private String name;

    @Schema(description = "참여자들 목록")
    private List<ParticipantUserDto> participants = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "미션 시작일자")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "미션 종료일자")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    @Builder
    public MissionResponseDto(Mission mission){
        this.title = mission.getTitle();
        this.content = mission.getContent();
        this.imgUrl = mission.getImageUrl();
        this.name = mission.getUser().getName();
        this.participants = mission.getAllParticipantUser();
        this.startDate = mission.getStartDate();
        this.endDate = mission.getEndDate();
    }
}
