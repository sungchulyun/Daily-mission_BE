package dailymissionproject.demo.domain.post.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dailymissionproject.demo.domain.post.repository.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "포스트 상세 응답 DTO")
public class PostResponseDto implements Serializable {

    @Schema(description = "포스트 PK ID")
    private Long postId;

    @Schema(description = "포스트 미션 ID")
    private Long missionId;

    @Schema(description = "포스트 미션 제목")
    private String missionTitle;

    @Schema(description = "포스트 작성자 이름")
    private String userName;

    @Schema(description = "포스트 작성자 이미지")
    private String userImgUrl;

    @Schema(description = "포스트 제목")
    private String title;
    @Schema(description = "포스트 내용")
    private String content;
    @Schema(description = "포스트 썸네일")
    private String imgUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDate;

    @Builder
    public PostResponseDto (Post post){
        this.postId = post.getId();
        this.missionId = post.getMission().getId();
        this.missionTitle = post.getMission().getTitle();

        this.userName = post.getUser().getName();
        this.userImgUrl = post.getUser().getImgUrl();

        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrl = post.getImageUrl();

        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }
}
