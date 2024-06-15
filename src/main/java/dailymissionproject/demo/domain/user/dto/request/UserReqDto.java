package dailymissionproject.demo.domain.user.dto.request;

import dailymissionproject.demo.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;
@Getter
public class UserReqDto {

    private final String name;
    private final String email;

    @Builder
    public UserReqDto(String name, String email){
        this.name = name;
        this.email = email;
    }
    @Builder
    public User toEntity(UserReqDto userReqDto){
        return User.builder()
                .name(userReqDto.getName())
                .email(userReqDto.getEmail())
                .build();
    }
}
