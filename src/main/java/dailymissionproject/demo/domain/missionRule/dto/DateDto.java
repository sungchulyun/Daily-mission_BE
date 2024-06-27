package dailymissionproject.demo.domain.missionRule.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DateDto {
    private LocalDate date;
    private String day;
    private boolean mandatory;

    @Builder
    public DateDto(LocalDate date, String day, boolean mandatory){
        this.date = date;
        this.day = day;
        this.mandatory =  mandatory;
    }
}
