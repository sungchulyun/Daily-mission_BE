package dailymissionproject.demo.domain.post.dto.request;

import dailymissionproject.demo.domain.post.repository.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private final String title;
    private final String content;
    @Builder
    public PostUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
