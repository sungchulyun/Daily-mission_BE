package dailymissionproject.demo.domain.like.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LikesResponseDto {

    private final Long postId;

    private final Long userId;

    private final boolean isLiked;

    @Builder
    public LikesResponseDto(Long postId, Long userId, boolean isLiked) {
        this.postId = postId;
        this.userId = userId;
        this.isLiked = isLiked;
    }
}
