package dailymissionproject.demo.domain.user.fixture;

import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;

public class UserObjectFixture {



    public static UserDetailResponseDto getUserResponse(){
        return UserDetailResponseDto.builder()
                .nickname("윤성철")
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
