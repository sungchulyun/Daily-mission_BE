package dailymissionproject.demo.domain.user.service;


import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.fixture.UserObjectFixture;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("[unit] [service] UserService")
@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserService userService;

    private final UserDetailResponseDto detailResponse = UserObjectFixture.getUserResponse();
    private final UserUpdateRequestDto updateRequest = UserObjectFixture.getUserUpdateRequest();
    private final UserUpdateResponseDto updateResponse = UserObjectFixture.getUserUpdateResponse();
    private CustomOAuth2User user;

    @BeforeEach
    void setUp() {
        user = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google", "sungchul", "aws-s3", "google@gmailcom"));
    }

    @Nested
    @DisplayName("유저 정보 조회 서비스 레이어 테스트")
    class UserDetailServiceTest{

        @Test
        @DisplayName("유저 상세 정보를 조회할 수 있다.")
        void get_user_detail_success() {
            //given

            //when
            when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.of(UserObjectFixture.getUserFixture()));

            UserDetailResponseDto userDetail = userService.detail(user);

            //then
            assertEquals(userDetail.getNickname(), detailResponse.getNickname());
        }
    }
}
