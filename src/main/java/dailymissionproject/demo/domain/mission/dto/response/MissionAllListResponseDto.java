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

    @Schema(description = "미션 PK ID")
    private Long id;
    @Schema(description = "미션 제목")
    private String title;
    @Schema(description = "미션 내용")
    private String content;
    @Schema(description = "미션 썸네일")
    private String imageUrl;
    @Schema(description = "미션 방장")
    private String username;

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

    @Schema(description = "미션 종료 여부")
    private boolean ended;

    @Builder
    MissionAllListResponseDto(Long id, String title, String content, String imageUrl, String username, LocalDate startDate, LocalDate endDate, boolean ended) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.username = username;

        this.startDate = startDate;
        this.endDate = endDate;
        this.ended = ended;
    }
}
