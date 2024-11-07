package dailymissionproject.demo.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "포스트 생성 응답 DTO")
public class PostSaveResponseDto {

    @Schema(description = "포스트 제목")
    private final String title;

    @Schema(description = "포스트 내용")
    private final String content;


    @Builder
    public PostSaveResponseDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
