package dailymissionproject.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "유저 정보 응답 DTO")
public class UserDetailResponseDto {

    @Schema(description = "유저 닉네임")
    private final String nickname;
    @Schema(description = "유저 이메일")
    private final String email;
    @Schema(description = "유저 이미지 썸네일")
    private final String imageUrl;

    @Builder
    public UserDetailResponseDto(String nickname, String email, String imageUrl) {
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
