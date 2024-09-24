package dailymissionproject.demo.domain.user.fixture;

import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;

public class UserObjectFixture {

    public static User getUserFixture(){
        return User.builder()
                .username("google 1923819273")
                .email("google@gmail.com")
                .nickname("sungchul")
                .imageUrl("https://aws-s3.jpg")
                .name("윤성철")
                .role(Role.USER)
                .build();
    }
    public static UserDetailResponseDto getUserResponse(){
        return UserDetailResponseDto.builder()
                .nickname("sungchul")
                .email("proattacker641@gmail.com")
                .imageUrl("https://aws-s3.jpg")
                .build();
    }

    public static UserUpdateRequestDto getUserUpdateRequest(){
        return UserUpdateRequestDto.builder()
                .nickname("sungchul")
                .build();
    }

    public static UserUpdateResponseDto getUserUpdateResponse(){
        return UserUpdateResponseDto.builder()
                .username("google 1923819273")
                .nickname("sungchul")
                .imageUrl("https://AWS-s3/modifedImages.jpg")
                .build();
    }
}
