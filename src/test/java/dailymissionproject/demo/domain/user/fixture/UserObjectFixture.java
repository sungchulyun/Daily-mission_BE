package dailymissionproject.demo.domain.user.fixture;

import dailymissionproject.demo.domain.user.dto.response.UserResDto;

public class UserObjectFixture {

    public static UserResDto getUserResponse(){
        return UserResDto.builder()
                .name("윤성철")
                .email("proattacker641@gmail.com")
                .imgUrl("https://aws-s3.jpg")
                .build();
    }
}
