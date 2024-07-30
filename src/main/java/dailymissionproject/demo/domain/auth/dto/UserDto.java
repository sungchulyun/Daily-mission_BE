package dailymissionproject.demo.domain.auth.dto;

import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {

    private String role;
    private String name;
    private String username;
    private String imageUrl;
    private String email;

    @Builder
    public User toEntity(UserDto userDto){
        return User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .imgUrl(userDto.getImageUrl())
                .email(userDto.getEmail())
                .role(Role.valueOf(userDto.getRole()))
                .build();
    }
}
