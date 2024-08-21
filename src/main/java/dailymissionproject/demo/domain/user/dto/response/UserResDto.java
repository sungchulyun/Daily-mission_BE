package dailymissionproject.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "유저 정보 응답 DTO")
public class UserResDto {

    @Schema(description = "유저 닉네임")
    private  String name;
    @Schema(description = "유저 이메일")
    private  String email;
    @Schema(description = "유저 이미지 썸네일")
    private  String imageUrl;

    @Builder
    UserResDto(String name, String email, String imageUrl){
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
