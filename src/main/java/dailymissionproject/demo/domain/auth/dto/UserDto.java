package dailymissionproject.demo.domain.auth.dto;

import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String role;
    private String name;
    private String username;
    private String nickname;
    private String imageUrl;
    private String email;

    public UserDto(long id, String role, String name, String username, String nickname, String imageUrl, String email) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.username = username;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    @Builder
    public User toEntity(UserDto userDto){
        return User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .nickname(userDto.getNickname())
                .imageUrl(userDto.getImageUrl())
                .email(userDto.getEmail())
                .role(Role.valueOf(userDto.getRole()))
                .build();
    }

    public void setId(Long id) {
        this.id = id;
    }
}
