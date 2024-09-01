package dailymissionproject.demo.domain.user.fixture;

import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;

public class UserObjectFixture {

    public static UserDetailResponseDto getUserResponse(){
        return UserDetailResponseDto.builder()
                .nickname("윤성철")
                .email("proattacker641@gmail.com")
                .imageUrl("https://aws-s3.jpg")
                .build();
    }
}
