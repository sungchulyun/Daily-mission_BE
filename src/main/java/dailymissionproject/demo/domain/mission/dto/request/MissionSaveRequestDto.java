package dailymissionproject.demo.domain.mission.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionSaveRequestDto {

    private final String title;
    private final String content;
    private final String imgUrl;

    @Builder
    MissionSaveRequestDto(String title, String content, String imgUrl){
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

}
