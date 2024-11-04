package dailymissionproject.demo.domain.post.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostMissionListResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostSaveResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUpdateResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUserListResponseDto;
import dailymissionproject.demo.domain.post.exception.PostException;
import dailymissionproject.demo.domain.post.exception.PostExceptionCode;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.MISSION_NOT_FOUND;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("[unit] [service] PostService")
@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class PostServiceTest {
    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private ParticipantRepository participantRepository;

    private final Mission mission = PostObjectFixture.getMissionFixture();
    private final User user = PostObjectFixture.getUserFixture();
    private final Post post = PostObjectFixture.getPostFixture();

    private final PostSaveRequestDto saveRequest = PostObjectFixture.getSaveRequest();
    private final PostUpdateRequestDto updateRequest = PostObjectFixture.getPostUpdateRequest();
    private final PostUpdateResponseDto updateResponse = PostObjectFixture.getPostUpdateResponse();
    private final List<PostUserListResponseDto> userListResponse = PostObjectFixture.getUserPostList();
    private final List<PostMissionListResponseDto> missionListResponse = PostObjectFixture.getMissionPostList();

    private final Long postId = 1L;
    private final Long missionId = 1L;
    private CustomOAuth2User oAuth2User;

    @BeforeEach
    void setUp(){
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google 1923819273", "sungchul", "aws-s3", "google@gmailcom"));
    }

    @Nested
    @DisplayName("포스트 생성 서비스 레이어 테스트")
    class PostSaveServiceTest {
        final String fileName = "userModifyImage";
        final String contentType = "image/jpeg";

        @Test
        @DisplayName("포스트를 생성할 수 있다.")
        void post_save_success() throws IOException {
            MockMultipartFile file = new MockMultipartFile("file", fileName, contentType, "test data".getBytes(StandardCharsets.UTF_8));

            when(missionRepository.findByIdAndDeletedIsFalse(anyLong())).thenReturn(Optional.of(mission));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            boolean result = postService.isParticipating(user, mission);
            PostSaveResponseDto saveResponse = postService.save(oAuth2User, saveRequest, file);

            verify(imageService, times(1)).uploadPostS3(any(), any());
            assertTrue(result);
            assertEquals(saveResponse.getTitle(), saveRequest.getTitle());
        }

        @Test
        @DisplayName("미션이 존재하지 않으면 포스트를 생성할 수 없다.")
        void post_save_fail_when_mission_not_exists() throws Exception {
            when(missionRepository.findByIdAndDeletedIsFalse(saveRequest.getMissionId())).thenThrow(new MissionException(MISSION_NOT_FOUND));

            MissionException missionException = assertThrows(MissionException.class, () -> postService.save(oAuth2User, saveRequest, any()));

            assertEquals(MISSION_NOT_FOUND, missionException.getExceptionCode());
        }

        @Test
        @DisplayName("유저 정보가 존재하지 않으면 포스트를 생성할 수 없다.")
        void post_save_fail_when_user_not_exists() throws Exception {
            when(missionRepository.findByIdAndDeletedIsFalse(saveRequest.getMissionId())).thenReturn(Optional.of(mission));
            when(userRepository.findById(any())).thenThrow(new UserException(USER_NOT_FOUND));

            UserException userException = assertThrows(UserException.class, () -> postService.save(oAuth2User, saveRequest, any()));
            assertEquals(USER_NOT_FOUND, userException.getExceptionCode());
        }

        @Test
        @DisplayName("유저가 해당 미션에 참여중이지 않은면 포스트를 생성할 수 없다.")
        void post_save_fail_when_user_is_not_participating() throws Exception {
            //given
            mission.setParticipants(new ArrayList<>());

            when(missionRepository.findByIdAndDeletedIsFalse(saveRequest.getMissionId())).thenReturn(Optional.of(mission));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            PostException postException = assertThrows(PostException.class, () -> postService.save(oAuth2User, saveRequest, any()));
            assertEquals(PostExceptionCode.INVALID_POST_SAVE_REQUEST, postException.getExceptionCode());
        }
    }


}
