package dailymissionproject.demo.domain.auth.dto;

import dailymissionproject.demo.domain.user.repository.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {

    private Role role;
    private String name;
    private String username;
    private String imageUrl;
    private String email;
}
