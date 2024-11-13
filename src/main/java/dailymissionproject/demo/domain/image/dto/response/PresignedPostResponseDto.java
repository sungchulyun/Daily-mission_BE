package dailymissionproject.demo.domain.image.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class PresignedPostResponseDto {
    private String url;
    private String path;

    @Builder
    public PresignedPostResponseDto(String url, String path) {
        this.url = url;
        this.path = path;
    }
}
