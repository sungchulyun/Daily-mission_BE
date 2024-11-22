package dailymissionproject.demo.domain.like.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LikesResponseDto {

    private final Long postId;

    private final Long userId;

    @Builder
    public LikesResponseDto(Long likeId, Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
