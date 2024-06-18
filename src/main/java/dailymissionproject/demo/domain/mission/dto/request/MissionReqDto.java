package dailymissionproject.demo.domain.mission.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionReqDto {

    private final String title;
    private final String content;
    private final String imgUrl;

    @Builder
    MissionReqDto(String title, String content, String imgUrl){
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

}
