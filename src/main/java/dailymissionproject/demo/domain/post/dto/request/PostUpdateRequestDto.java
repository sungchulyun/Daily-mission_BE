package dailymissionproject.demo.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "포스트 업데이트 요청 DTO")
public class PostUpdateRequestDto {

    @Schema(description = "포스트 제목")
    private final String title;

    @Schema(description = "포스트 내용")
    private final String content;

    @Schema(description = "포스트 이미지")
    private final String imageUrl;

    @Builder
    public PostUpdateRequestDto(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

}
