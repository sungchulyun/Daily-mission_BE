package dailymissionproject.demo.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "유저별 포스트 리스트 응답 DTO")
@NoArgsConstructor(force = true)
public class PostUserListResponseDto {

    @Schema(description = "포스트 PK ID")
    private final Long id;

    @Schema(description = "포스트 미션 ID")
    private final Long missionId;

    @Schema(description = "포스트 미션 제목")
    private final String missionTitle;

    @Schema(description = "포스트 제목")
    private final String title;
    @Schema(description = "포스트 내용")
    private final String content;
    @Schema(description = "포스트 썸네일")
    private final String imageUrl;

    @Builder
    public PostUserListResponseDto(Long id, Long missionId, String missionTitle, String title, String content, String imageUrl) {
        this.id = id;
        this.missionId = missionId;
        this.missionTitle = missionTitle;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
