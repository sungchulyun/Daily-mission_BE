package dailymissionproject.demo.domain.like.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LikeResponseDto {

    private final Long likeId;

    private final Long postId;

    private final Long userId;

    @Builder
    public LikeResponseDto(Long likeId, Long postId, Long userId) {
        this.likeId = likeId;
        this.postId = postId;
        this.userId = userId;
    }
}
