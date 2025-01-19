package dailymissionproject.demo.domain.like.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "좋아요 응답")
public class LikesResponseDto {

    @Schema(description = "포스트 ID")
    private final Long postId;

    @Schema(description = "유저 ID")
    private final Long userId;

    @Schema(description = "좋아요 여부")
    private final boolean isLiked;

    @Builder
    public LikesResponseDto(Long postId, Long userId, boolean isLiked) {
        this.postId = postId;
        this.userId = userId;
        this.isLiked = isLiked;
    }
}
