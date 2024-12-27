package dailymissionproject.demo.domain.post.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.notify.service.NotificationService;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.*;
import dailymissionproject.demo.domain.post.exception.PostException;
import dailymissionproject.demo.domain.post.exception.PostExceptionCode;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.MISSION_NOT_FOUND;
import static dailymissionproject.demo.domain.post.exception.PostExceptionCode.INVALID_USER_REQUEST;
import static dailymissionproject.demo.domain.post.exception.PostExceptionCode.POST_NOT_FOUND;
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
    private NotificationService notificationService;

    private final Mission mission = PostObjectFixture.getMissionFixture();
    private final User user = PostObjectFixture.getUserFixture();
    private final Post post = PostObjectFixture.getPostFixture();

    private final PostSaveRequestDto saveRequest = PostObjectFixture.getSaveRequest();
    private final PostDetailResponseDto detailResponse = PostObjectFixture.getDetailResponse();
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

        @Test
        @DisplayName("포스트를 생성할 수 있다.")
        void post_save_success() throws IOException {
            User poster = new User(1L, "윤성철", "proattacker@naver.com", "성철");
            when(missionRepository.findByIdAndDeletedIsFalse(anyLong())).thenReturn(Optional.of(mission));
            when(userRepository.findById(any())).thenReturn(Optional.of(poster));

            PostSaveResponseDto saveResponse = postService.save(oAuth2User, saveRequest);


            assertEquals(saveResponse.getTitle(), saveRequest.getTitle());
        }

        @Test
        @DisplayName("미션이 존재하지 않으면 포스트를 생성할 수 없다.")
        void post_save_fail_when_mission_not_exists() throws Exception {
            when(missionRepository.findByIdAndDeletedIsFalse(saveRequest.getMissionId())).thenThrow(new MissionException(MISSION_NOT_FOUND));

            MissionException missionException = assertThrows(MissionException.class, () -> postService.save(oAuth2User, saveRequest));

            assertEquals(MISSION_NOT_FOUND, missionException.getExceptionCode());
        }

        @Test
        @DisplayName("유저 정보가 존재하지 않으면 포스트를 생성할 수 없다.")
        void post_save_fail_when_user_not_exists() throws Exception {
            when(missionRepository.findByIdAndDeletedIsFalse(saveRequest.getMissionId())).thenReturn(Optional.of(mission));
            when(userRepository.findById(any())).thenThrow(new UserException(USER_NOT_FOUND));

            UserException userException = assertThrows(UserException.class, () -> postService.save(oAuth2User, saveRequest));
            assertEquals(USER_NOT_FOUND, userException.getExceptionCode());
        }

        @Test
        @DisplayName("유저가 해당 미션에 참여중이지 않은면 포스트를 생성할 수 없다.")
        void post_save_fail_when_user_is_not_participating() throws Exception {
            //given
            mission.setParticipants(new ArrayList<>());

            when(missionRepository.findByIdAndDeletedIsFalse(saveRequest.getMissionId())).thenReturn(Optional.of(mission));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            PostException postException = assertThrows(PostException.class, () -> postService.save(oAuth2User, saveRequest));
            assertEquals(PostExceptionCode.INVALID_POST_SAVE_REQUEST, postException.getExceptionCode());
        }
    }

    @Nested
    @DisplayName("포스트 조회 서비스 레이어 테스트")
    class PostReadServiceTest {

        @Test
        @DisplayName("포스트 상세조회할 수 있다.")
        void post_read_success() throws IOException {
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));

            PostDetailResponseDto response = postService.findById(postId);

            verify(postRepository, times(1)).findById(postId);
            assertEquals(detailResponse.getId(), response.getId());
            assertEquals(detailResponse.getLikesCount(), response.getLikesCount());
        }

        @Test
        @DisplayName("존재하지 않는 포스트는 조회할 수 없다.")
        void post_read_fail_when_post_is_not_exists() throws Exception {
            when(postRepository.findById(postId)).thenThrow(new PostException(POST_NOT_FOUND));

            PostException postException = assertThrows(PostException.class, () -> postService.findById(postId));

            assertEquals(POST_NOT_FOUND, postException.getExceptionCode());
        }

        @Test
        @DisplayName("유저가 작성한 포스트 리스트를 조회할 수 있다.")
        void post_user_list_read_success() throws Exception {
            //given
            Pageable pageable = PageRequest.of(0, 3);

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(postRepository.findAllByUser(pageable, user))
                    .thenReturn(new SliceImpl<>(userListResponse, pageable, false));

            PageResponseDto response = postService.findAllByUser(oAuth2User, pageable);
            List<PostUserListResponseDto> responseList = (List<PostUserListResponseDto>) response.content();

            assertEquals(responseList.get(0).getTitle(), userListResponse.get(0).getTitle());
            assertEquals(responseList.get(0).getContent(), userListResponse.get(0).getContent());
            assertEquals(responseList.get(0).getId(), userListResponse.get(0).getId());
            assertEquals(responseList.get(0).getLikeCount(), userListResponse.get(0).getLikeCount());
        }

        @Test
        @DisplayName("유저가 존재하지 않으면 포스트 리스트를 조회할 수 없다.")
        void post_user_list_read_fail_when_user_is_not_exits() throws Exception {
            //given
            Pageable pageable = PageRequest.of(0, 3);

            when(userRepository.findById(any())).thenThrow(new UserException(USER_NOT_FOUND));

            UserException userException = assertThrows(UserException.class, () -> postService.findAllByUser(oAuth2User, pageable));
            assertEquals(USER_NOT_FOUND, userException.getExceptionCode());
        }

        @Test
        @DisplayName("미션별 포스트 리스트를 조회할 수 있다.")
        void post_mission_list_read_success() throws Exception {
            //given
            Pageable pageable = PageRequest.of(0, 3);

            when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
            when(postRepository.findAllByMission(pageable, mission))
                    .thenReturn(new SliceImpl<>(missionListResponse, pageable, false));

            PageResponseDto pageResponseDto = postService.findAllByMission(missionId, pageable);
            List<PostMissionListResponseDto> responseList = (List<PostMissionListResponseDto>) pageResponseDto.content();

            assertEquals(responseList.get(0).getTitle(), missionListResponse.get(0).getTitle());
            assertEquals(responseList.get(0).getContent(), missionListResponse.get(0).getContent());
            assertEquals(responseList.get(0).getId(), missionListResponse.get(0).getId());
        }

        @Test
        @DisplayName("미션이 존재하지 않으면 리스트를 조회할 수 없다.")
        void post_mission_list_read_fail_when_mission_is_not_exists() throws Exception {
            Pageable pageable = PageRequest.of(0, 3);

            when(missionRepository.findById(any())).thenThrow(new MissionException(MISSION_NOT_FOUND));

            MissionException missionException = assertThrows(MissionException.class, () -> postService.findAllByMission(missionId, pageable));
            assertEquals(MISSION_NOT_FOUND, missionException.getExceptionCode());
        }
    }

    @Nested
    @DisplayName("포스트 수정 서비스 레이어 테스트")
    class PostUpdateServiceTest {

        @Test
        @DisplayName("포스트를 수정할 수 있다.")
        void post_update_success() throws IOException {
            final String fileName = "userModifyImage";
            final String contentType = "image/jpeg";
            MockMultipartFile file = new MockMultipartFile("file", fileName, contentType, "test data".getBytes(StandardCharsets.UTF_8));

            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            PostUpdateResponseDto response = postService.update(postId, updateRequest, oAuth2User);

            assertEquals(response.getTitle(), updateRequest.getTitle());
            assertEquals(response.getContent(), updateRequest.getContent());
        }

        @Test
        @DisplayName("포스트가 존재하지 않으면 수정할 수 없다.")
        void post_update_fail_when_user_is_not_writer() throws Exception {
            when(postRepository.findById(postId)).thenThrow(new PostException(POST_NOT_FOUND));

            PostException postException = assertThrows(PostException.class, () -> postService.findById(postId));
            assertEquals(POST_NOT_FOUND, postException.getExceptionCode());
        }

        @Test
        @DisplayName("유저가 존재하지 않으면 수정할 수 없다.")
        void post_update_fail_when_user_is_not_exists() throws Exception {
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(userRepository.findById(any())).thenThrow(new UserException(USER_NOT_FOUND));

            UserException userException = assertThrows(UserException.class, () -> postService.update(postId, updateRequest, oAuth2User));
            assertEquals(USER_NOT_FOUND, userException.getExceptionCode());
        }

        @Test
        @DisplayName("포스트의 작성자가 아니면 수정할 수 없다.")
        void post_update_fail_when_not_writer() throws Exception {
            //given
            Post post_1 = Post.builder()
                    .user(new User(100L, "NAME", "EMAIL.COM", "NICKNAME"))
                    .build();

            when(postRepository.findById(any())).thenReturn(Optional.of(post_1));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            PostException postException = assertThrows(PostException.class, () -> postService.update(postId, updateRequest, oAuth2User));

            assertEquals(INVALID_USER_REQUEST, postException.getExceptionCode());
        }
    }

    @Nested
    @DisplayName("포스트 삭제 서비스 레이어 테스트")
    class PostDeleteServiceTest {

        @Test
        @DisplayName("포스트를 삭제할 수 있다.")
        void post_delete_success() throws IOException {
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            boolean result = postService.deleteById(postId, oAuth2User);

            assertTrue(result);
            assertEquals(post.isDeleted(), true);
        }

        @Test
        @DisplayName("포스트 작성자가 아니라면 삭제할 수 없다.")
        void post_delete_fail_when_user_is_not_writer() throws Exception {
            //given
            Post post_1 = Post.builder()
                    .user(new User(100L, "NAME", "EMAIL.COM", "NICKNAME"))
                    .build();

            when(postRepository.findById(any())).thenReturn(Optional.of(post_1));
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            PostException postException = assertThrows(PostException.class, () -> postService.deleteById(postId, oAuth2User));

            assertEquals(INVALID_USER_REQUEST, postException.getExceptionCode());
        }

        @Test
        @DisplayName("포스트가 존재하지 않으면 수정할 수 없다.")
        void post_delete_fail_when_post_is_not_exits() throws Exception {
            when(postRepository.findById(postId)).thenThrow(new PostException(POST_NOT_FOUND));

            PostException postException = assertThrows(PostException.class, () -> postService.deleteById(postId, oAuth2User));
            assertEquals(POST_NOT_FOUND, postException.getExceptionCode());
        }

        @Test
        @DisplayName("유저가 존재하지 않으면 수정할 수 없다.")
        void post_delete_fail_when_user_is_not_exists() throws Exception {
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(userRepository.findById(any())).thenThrow(new UserException(USER_NOT_FOUND));

            UserException userException = assertThrows(UserException.class, () -> postService.deleteById(postId, oAuth2User));
            assertEquals(USER_NOT_FOUND, userException.getExceptionCode());
        }
    }
}
