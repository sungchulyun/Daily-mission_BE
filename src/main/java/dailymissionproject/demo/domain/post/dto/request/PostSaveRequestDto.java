package dailymissionproject.demo.domain.post.dto.request;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "포스트 생성 요청 DTO")
public class PostSaveRequestDto {

    @Schema(description = "미션 PK ID")
    private final Long missionId;

    @Schema(description = "포스트 제목")
    @NotEmpty
    private final String title;

    @Schema(description = "포스트 내용")
    @NotEmpty
    private final String content;

    @Schema(description = "포스트 이미지")
    private final String imageUrl;

    @Builder
    public PostSaveRequestDto(Long missionId, String title, String content, String imageUrl) {
        this.missionId = missionId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public Post toEntity(User user, Mission mission){
        return Post.builder()
                .mission(mission)
                .user(user)
                .title(this.title)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .build();
    }


}
