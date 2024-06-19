package dailymissionproject.demo.domain.mission.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class MissionSaveResponseDto {

    private String title;
    private String content;
    private String imgUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public MissionSaveResponseDto(String title, String content, String imgUrl, LocalDate startDate, LocalDate endDate){
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
