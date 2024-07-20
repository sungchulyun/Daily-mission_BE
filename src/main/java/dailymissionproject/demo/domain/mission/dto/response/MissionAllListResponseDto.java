package dailymissionproject.demo.domain.mission.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "모든 미션 목록")
public class MissionAllListResponseDto {

    @Schema(description = "미션 제목")
    private String title;
    @Schema(description = "미션 내용")
    private String content;
    @Schema(description = "미션 썸네일")
    private String imgUrl;
    @Schema(description = "미션 방장")
    private String name;

    @Schema(description = "미션 시작일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @Schema(description = "미션 종료일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    @Builder
    MissionAllListResponseDto(String title, String content, String imgUrl, String name, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.name = name;

        this.startDate = startDate;
        this.endDate = endDate;
    }
}
