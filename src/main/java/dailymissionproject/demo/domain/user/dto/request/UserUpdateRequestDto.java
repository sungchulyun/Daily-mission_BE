package dailymissionproject.demo.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(force = true)
public class UserUpdateRequestDto implements Serializable {
    private final String nickname;
    private final String imageUrl;

    @Builder
    UserUpdateRequestDto(String nickname, String imageUrl){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
