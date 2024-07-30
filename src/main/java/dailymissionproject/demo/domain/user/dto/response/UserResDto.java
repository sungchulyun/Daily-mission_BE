package dailymissionproject.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "유저 정보 응답 DTO")
public class UserResDto {

    @Schema(description = "유저 닉네임")
    private final String name;
    @Schema(description = "유저 이메일")
    private final String email;
    @Schema(description = "유저 이미지 썸네일")
    private final String imgUrl;

    @Builder
    UserResDto(String name, String email, String imgUrl){
        this.name = name;
        this.email = email;
        this.imgUrl = imgUrl;
    }
}
