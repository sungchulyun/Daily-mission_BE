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

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "포스트 상세 응답 DTO")
public class PostDetailResponseDto {

    @Schema(description = "포스트 PK ID")
    private final Long id;

    @Schema(description = "포스트 미션 ID")
    private final Long missionId;

    @Schema(description = "포스트 미션 제목")
    private final String missionTitle;

    @Schema(description = "포스트 작성자 닉네임")
    private final String nickname;

    @Schema(description = "포스트 작성자 이미지")
    private final String userImageUrl;

    @Schema(description = "포스트 제목")
    private final String title;
    @Schema(description = "포스트 내용")
    private final String content;
    @Schema(description = "포스트 썸네일")
    private final String imageUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime modifiedDate;

    @Builder
    public PostDetailResponseDto(Post post){
        this.id = post.getId();
        this.missionId = post.getMission().getId();
        this.missionTitle = post.getMission().getTitle();
        this.nickname = post.getUser().getNickname();
        this.userImageUrl = post.getUser().getImageUrl();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }

    @Builder
    public PostDetailResponseDto(Long id, Long missionId, String missionTitle, String nickname, String userImageUrl, String title, String content, String imageUrl, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.missionId = missionId;
        this.missionTitle = missionTitle;
        this.nickname = nickname;
        this.userImageUrl = userImageUrl;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


    public static PostDetailResponseDto from(Post post){
        return new PostDetailResponseDto(post);
    }
}
