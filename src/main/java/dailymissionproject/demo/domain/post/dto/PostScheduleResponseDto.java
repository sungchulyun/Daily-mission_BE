package dailymissionproject.demo.domain.post.dto;

import dailymissionproject.demo.domain.missionRule.dto.DateDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostScheduleResponseDto {
    private List<DateDto> weekDates = new ArrayList<>();
    private List<PostHistoryDto> histories = new ArrayList<>();

    @Builder
    public PostScheduleResponseDto(List<DateDto> weekDates, List<PostHistoryDto> histories){

        this.weekDates = weekDates;

        this.histories = histories;
    }
}
