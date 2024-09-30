package dailymissionproject.demo.domain.user.service;


import dailymissionproject.demo.common.util.S3Util;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.fixture.UserObjectFixture;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("[unit] [service] UserService")
@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;
    @Mock
    private S3Util s3Util;

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

        @DisplayName("로그인 정보가 없다면 조회할 수 없다.")
        @Test
        void get_user_detail_fail(){

            //when
            when(userRepository.findById(anyLong())).thenThrow(new UserException(UserExceptionCode.USER_NOT_FOUND));
            //then
            assertThrows(UserException.class, () -> userService.detail(user));
        }
    }

    @Nested
    @DisplayName("유저 정보 수정 서비스 레이어 테스트")
    class UserModifyServiceTest{

        @Test
        @DisplayName("유저 정보를 수정할 수 있다.")
        void update_user_detail_success() throws IOException {
            final String fileName = "userModifyImage";
            final String contentType = "image/jpeg";
            //given
            MockMultipartFile file = new MockMultipartFile("file", fileName, contentType, "test data".getBytes(StandardCharsets.UTF_8));

            //when
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(UserObjectFixture.getUserFixture()));

            userService.updateProfile(user, updateRequest, file);

            //then
            verify(imageService, times(1)).uploadUserS3(any(), any());
        }
    }
}
