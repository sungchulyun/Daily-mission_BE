package dailymissionproject.demo.domain.post.dto.response;

import dailymissionproject.demo.domain.post.repository.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long postId;
    private Long missionId;
    private String missionTitle;

    private String userName;
    private String userImgUrl;

    private String title;
    private String content;
    private String imgUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'THH:mm:ss")
    private LocalDateTime modifiedDate;

    @Builder
    public PostResponseDto (Post post){
        this.postId = post.getId();
        this.missionId = post.getMission().getId();
        this.missionTitle = post.getMission().getTitle();

        this.userName = post.getUser().getName();
        this.userImgUrl = post.getUser().getImageUrl();

        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrl = post.getImageUrl();

        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }
}
