package dailymissionproject.demo.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "preSigned-url 응답 DTO")
public class PresignedPostResponseDto {

    @Schema(description = "presigned-url")
    private String url;
    @Schema(description = "파일 접근 경로")
    private String path;

    @Builder
    public PresignedPostResponseDto(String url, String path) {
        this.url = url;
        this.path = path;
    }
}
