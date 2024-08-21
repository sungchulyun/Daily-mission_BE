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
@NoArgsConstructor
@Schema(description = "포스트 상세 응답 DTO")
public class PostResponseDto {

    @Schema(description = "포스트 PK ID")
    private Long id;

    @Schema(description = "포스트 미션 ID")
    private Long missionId;

    @Schema(description = "포스트 미션 제목")
    private String missionTitle;

    @Schema(description = "포스트 작성자 이름")
    private String username;

    @Schema(description = "포스트 작성자 이미지")
    private String userImageUrl;

    @Schema(description = "포스트 제목")
    private String title;
    @Schema(description = "포스트 내용")
    private String content;
    @Schema(description = "포스트 썸네일")
    private String imageUrl;

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
        this.id = post.getId();
        this.missionId = post.getMission().getId();
        this.missionTitle = post.getMission().getTitle();
        this.username = post.getUser().getUsername();
        this.userImageUrl = post.getUser().getImageUrl();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }

    @Builder
    public PostResponseDto(Long id, Long missionId, String missionTitle, String username, String userImageUrl, String title
                        ,String content, String imageUrl, LocalDateTime createdDate, LocalDateTime modifiedDate){
        this.id = id;
        this.missionId = missionId;
        this.missionTitle = missionTitle;
        this.username = username;
        this.userImageUrl = userImageUrl;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
