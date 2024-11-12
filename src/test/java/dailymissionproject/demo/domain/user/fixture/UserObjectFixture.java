package dailymissionproject.demo.domain.user.fixture;

import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;

public class UserObjectFixture {

    /**
     * 유저 엔티티 fixture를 반환합니다.
     * @return
     */
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

    /**
     * 유저 상세 응답 객체를 반환합니다.
     * @return
     */
    public static UserDetailResponseDto getUserResponse(){
        return UserDetailResponseDto.builder()
                .nickname("sungchul")
                .email("proattacker641@gmail.com")
                .imageUrl("https://aws-s3.jpg")
                .build();
    }

    /**
     * 유저 수정 요청 객체를 반환합니다.
     * @return
     */
    public static UserUpdateRequestDto getUserUpdateRequest(){
        return UserUpdateRequestDto.builder()
                .imageUrl("https://integration-test-profile-image.jpeg")
                .nickname("TestSungchul")
                .build();
    }

    public static UserUpdateRequestDto getDuplicateNicknameUpdateRequest(){
        return UserUpdateRequestDto.builder()
                .imageUrl("https://lh3.googleusercontent.com/a/ACg8ocLuIomy21grZAe-_HDhHm7HDPbL6R9_5a1JY_i3o4-KutpPdw=s96-c")
                .nickname("sungchul")
                .build();
    }

    /**
     * 유저 수정 응답 객체를 반환합니다.
     * @return
     */
    public static UserUpdateResponseDto getUserUpdateResponse(){
        return UserUpdateResponseDto.builder()
                .username("naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA")
                .nickname("TestSungchul")
                .imageUrl("https://integration-test-profile-image.jpeg")
                .build();
    }
}
