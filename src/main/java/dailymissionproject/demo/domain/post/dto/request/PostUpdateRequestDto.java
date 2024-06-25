package dailymissionproject.demo.domain.post.dto.request;

import dailymissionproject.demo.domain.post.repository.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private final Long missionId;
    private final String title;
    private final String content;
    private final String imgUrl;
    @Builder
    public PostUpdateRequestDto(Long missionId, String title, String content, String imgUrl) {
        this.missionId = missionId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

}
