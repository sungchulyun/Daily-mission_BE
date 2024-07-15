package dailymissionproject.demo.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "신규 미션 목록")
public class MissionNewListResponseDto {

    @Schema(description = "미션 제목")
    private String title;
    @Schema(description = "미션 내용")
    private String content;
    @Schema(description = "미션 썸네일")
    private String imgUrl;
    @Schema(description = "미션 방장")
    private String userName;

    @Schema(description = "미션 시작일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "미션 종료일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    MissionNewListResponseDto(String title, String content, String imgUrl, String userName, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.userName = userName;

        this.startDate = startDate;
        this.endDate = endDate;
    }
}
