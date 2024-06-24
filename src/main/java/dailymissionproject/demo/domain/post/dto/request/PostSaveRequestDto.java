package dailymissionproject.demo.domain.post.dto.request;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSaveRequestDto {
    private final Long missionId;
    private final String title;
    private final String content;
    private final String imgUrl;
    @Builder
    public PostSaveRequestDto(Long missionId, String title, String content, String imgUrl) {
        this.missionId = missionId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public Post toEntity(User user, Mission mission){
        return Post.builder()
                .mission(mission)
                .user(user)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .build();
    }


}
