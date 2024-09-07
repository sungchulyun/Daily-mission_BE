package dailymissionproject.demo.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(force = true)
public class UserUpdateResponseDto implements Serializable {

    private final String username;
    private final String nickname;
    private final String imageUrl;

    @Builder
    public UserUpdateResponseDto(String username, String nickname, String imageUrl) {
        this.username = username;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
