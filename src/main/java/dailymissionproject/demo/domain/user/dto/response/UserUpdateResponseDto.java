package dailymissionproject.demo.domain.user.dto.response;

import lombok.Builder;

public class UserUpdateResponseDto {

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
