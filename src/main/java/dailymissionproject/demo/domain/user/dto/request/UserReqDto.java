package dailymissionproject.demo.domain.user.dto.request;

import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class UserReqDto {

    String name;
    String email;
    String picture;

    @Builder
    public UserReqDto(String name, String email, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    @Builder
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .build();
    }
}
