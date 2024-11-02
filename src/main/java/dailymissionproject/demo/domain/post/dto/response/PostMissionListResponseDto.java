package dailymissionproject.demo.domain.post.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Schema(description = "미션별 포스트 리스트 응답 DTO")
@NoArgsConstructor(force = true)
public class PostMissionListResponseDto {
    @Schema(description = "포스트 PK ID")
    private final Long id;

    @Schema(description = "포스트 미션 ID")
    private final Long missionId;

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
    public PostMissionListResponseDto(Long id, Long missionId, String nickname, String userImageUrl, String title, String content, String imageUrl, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.missionId = missionId;
        this.nickname = nickname;
        this.userImageUrl = userImageUrl;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
