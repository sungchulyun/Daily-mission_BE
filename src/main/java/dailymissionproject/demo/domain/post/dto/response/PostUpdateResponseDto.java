package dailymissionproject.demo.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "포스트 업데이트 응답 DTO")
@NoArgsConstructor(force = true)
public class PostUpdateResponseDto {

    @Schema(description = "포스트 수정 제목")
    private final String title;

    @Schema(description = "포스트 수정 내용")
    private final String content;

    @Schema(description = "포스트 수정 이미지")
    private final String imageUrl;

    @Builder
    public PostUpdateResponseDto(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
