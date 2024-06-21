package dailymissionproject.demo.domain.mission.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MissionAllListResponseDto {
    private String title;
    private String content;
    private String imgUrl;
    private String userName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    MissionAllListResponseDto(String title, String content, String imgUrl, String userName, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.userName = userName;

        this.startDate = startDate;
        this.endDate = endDate;
    }
}
