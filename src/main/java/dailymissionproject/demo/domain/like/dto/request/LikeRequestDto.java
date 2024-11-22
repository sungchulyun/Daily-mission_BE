package dailymissionproject.demo.domain.like.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LikeRequestDto {

    private final Long postId;

    private final Long userId;

    @Builder
    public LikeRequestDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
