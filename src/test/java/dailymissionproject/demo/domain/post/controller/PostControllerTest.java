package dailymissionproject.demo.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.*;
import dailymissionproject.demo.domain.post.exception.PostException;
import dailymissionproject.demo.domain.post.exception.PostExceptionCode;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.post.service.PostService;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@Tag("unit")
@DisplayName("[unit] [controller] PostController")
@WebMvcTest(controllers = PostController.class)
@WithMockCustomUser
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;

    private final PostSaveRequestDto saveRequest = PostObjectFixture.getSaveRequest();
    private final PostSaveResponseDto saveResponse = PostObjectFixture.getSaveResponse();
    private final PostDetailResponseDto detailResponse = PostObjectFixture.getDetailResponse();
    private final List<PostUserListResponseDto> userPostList = PostObjectFixture.getUserPostList();
    private final List<PostMissionListResponseDto> missionPostList = PostObjectFixture.getMissionPostList();
    private final PostUpdateRequestDto updateRequest = PostObjectFixture.getPostUpdateRequest();
    private final PostUpdateResponseDto updateResponse = PostObjectFixture.getPostUpdateResponse();


    public static CustomOAuth2User oAuth2User;
    private final Long missionId = 1L;
    private final Long postId = 1L;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google 1923819273", "sungchul", "aws-s3", "google@gmailcom"));
    }

    @Nested
    @DisplayName("포스트 생성 컨트롤러 테스트")
    class PostSaveControllerTest {
        @Test
        @DisplayName("포스트를 생성 할 수 있다.")
        void post_save_success() throws Exception {
            when(postService.save(any(), any(PostSaveRequestDto.class))).thenReturn(saveResponse);

            mockMvc.perform(post("/api/v1/post/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(saveRequest))
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            verify(postService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(), any(PostSaveRequestDto.class));
        }

        @Test
        @DisplayName("미션이 존재하지 않을경우 포스트 생성에 실패한다.")
        void post_save_fail() throws Exception {
            when(postService.save(any(), any())).thenThrow(new MissionException(MissionExceptionCode.MISSION_NOT_FOUND));

            mockMvc.perform(post("/api/v1/post/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(saveRequest))
                    .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(CustomOAuth2User.class), any());
        }

        @Test
        @DisplayName("미션 참여자가 아닐경우 포스트 생성에 실패한다.")
        void post_save_fail_when_user_is_not_participating() throws Exception {
            when(postService.save(any(), any())).thenThrow(new PostException(PostExceptionCode.INVALID_POST_SAVE_REQUEST));

            mockMvc.perform(post("/api/v1/post/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(saveRequest))
                    .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(CustomOAuth2User.class), any());
        }
    }

    @Nested
    @DisplayName("포스트 조회 컨트롤러 테스트")
    class PostReadControllerTest {

        @Test
        @DisplayName("포스트를 상세조회할 수 있다.")
        void post_read_success() throws Exception {
            when(postService.findById(postId)).thenReturn(detailResponse);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/post/{id}", postId)
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.data.title").value(detailResponse.getTitle()));
            resultActions.andExpect(jsonPath("$.data.content").value(detailResponse.getContent()));
            resultActions.andExpect(jsonPath("$.data.missionTitle").value(detailResponse.getMissionTitle()));
            resultActions.andExpect(jsonPath("$.data.imageUrl").value(detailResponse.getImageUrl()));
        }

        @Test
        @DisplayName("포스트가 존재하지 않으면 포스트를 조회할 수 없다.")
        void post_read_fail_when_post_not_exists() throws Exception {
            when(postService.findById(postId)).thenThrow(new PostException(PostExceptionCode.POST_NOT_FOUND));

            mockMvc.perform(get("/api/v1/post/{id}", postId)
                    .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("findById 메서드가 정상적으로 호출됨"))
                    .findById(postId);
        }

        @Test
        @DisplayName("유저가 작성한 포스트 전체를 조회할 수 있다.")
        void post_read_user_list_success() throws Exception {
            //given
            Pageable pageable = PageRequest.of(0, 3);
            Slice<PostUserListResponseDto> sliceList = new SliceImpl<>(userPostList, pageable, false);
            PageResponseDto response = new PageResponseDto<>(sliceList, false);

            when(postService.findAllByUser(any(), any())).thenReturn(response);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/post/user")
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.data.content[0].content").value(userPostList.get(0).getContent()));
            resultActions.andExpect(jsonPath("$.data.content[0].title").value(userPostList.get(0).getTitle()));
        }

        @Test
        @DisplayName("유저 정보가 없을경우 예외를 반환한다.")
        void post_read_user_list_fail_when_user_not_exits() throws Exception {
            when(postService.findAllByUser(any(), any())).thenThrow(new UserException(UserExceptionCode.USER_NOT_FOUND));

            mockMvc.perform(get("/api/v1/post/user")
                    .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("findAllByUser 메서드가 정상 호출됨"))
                    .findAllByUser(any(), any());
        }

        @Test
        @DisplayName("미션별 작성된 포스트 전체를 조회할 수 있다.")
        void post_read_mission_list_success() throws Exception {
            //given
            Pageable pageable = PageRequest.of(0, 3);
            Slice<PostMissionListResponseDto> sliceList = new SliceImpl<>(missionPostList, pageable, false);
            PageResponseDto response = new PageResponseDto<>(sliceList, false);

            when(postService.findAllByMission(any(), any())).thenReturn(response);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/post/mission/{id}", missionId)
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.data.content[0].content").value(missionPostList.get(0).getContent()));
            resultActions.andExpect(jsonPath("$.data.content[0].title").value(missionPostList.get(0).getTitle()));
        }

        @Test
        @DisplayName("미션 정보가 없을경우 예외를 반환한다.")
        void post_read_mission_list_fail_when_mission_not_exits() throws Exception {
            when(postService.findAllByMission(any(), any())).thenThrow(new MissionException(MissionExceptionCode.MISSION_NOT_FOUND));

            mockMvc.perform(get("/api/v1/post/mission/{id}", missionId)
                    .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("findAllByMission 메서드가 정상 호출됨"))
                    .findAllByMission(any(), any());
        }
    }

    @Nested
    @DisplayName("포스트 수정 컨트롤러 테스트")
    class PostUpdateControllerTest {
        final String fileName = "https://AWS-S3/postThumbnail.jpg";
        final String contentType = "image/jpeg";

        @Test
        @DisplayName("포스트를 수정할 수 있다.")
        void post_update_success() throws Exception {
            when(postService.update(anyLong(), any(), any())).thenReturn(updateResponse);

            mockMvc.perform(put("/api/v1/post/{id}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest))
                    .with(csrf()))
                    .andExpect(status().isOk());

            verify(postService, description("update 메서드가 정상 호출됨"))
                    .update(anyLong(), any(), any());
        }

        @Test
        @DisplayName("포스트가 없을경우 예외를 반환한다.")
        void post_update_fail() throws Exception {
            //given
            when(postService.update(anyLong(), any(), any())).thenThrow(new PostException(PostExceptionCode.POST_NOT_FOUND));

            mockMvc.perform(put("/api/v1/post/{id}", postId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("update 메서드가 정상 호출됨"))
                    .update(anyLong(), any(), any());
        }

        @Test
        @DisplayName("포스트 작성자가 아닐경우 예외를 반환한다.")
        void post_update_fail_when_user_is_not_writer() throws Exception {
            //given
            when(postService.update(anyLong(), any(), any())).thenThrow(new PostException(PostExceptionCode.INVALID_USER_REQUEST));

            mockMvc.perform(put("/api/v1/post/{id}", postId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("update 메서드가 정상 호출됨"))
                    .update(anyLong(), any(), any());
        }
    }

    @Nested
    @DisplayName("포스트 삭제 컨트롤러 테스트")
    class PostDeleteControllerTest {

        @Test
        @DisplayName("포스트를 삭제할 수 있다.")
        void post_delete_success() throws Exception {
            when(postService.deleteById(any(), any())).thenReturn(true);

            mockMvc.perform(delete("/api/v1/post/{id}", postId)
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            verify(postService,  description("deleteById 메서드가 정상 호출됨"))
                    .deleteById(any(), any());
        }

        @Test
        @DisplayName("미션이 없을경우 예외를 반환한다.")
        void post_delete_fail_when_mission_not_exits() throws Exception {
            when(postService.deleteById(any(), any())).thenThrow(new MissionException(MissionExceptionCode.MISSION_NOT_FOUND));

            mockMvc.perform(delete("/api/v1/post/{id}", postId)
                    .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("유저 정보가 없을경우 예외를 반환한다.")
        void post_delete_fail_when_user_not_exits() throws Exception {
            when(postService.deleteById(any(), any())).thenThrow(new UserException(UserExceptionCode.USER_NOT_FOUND));

            mockMvc.perform(delete("/api/v1/post/{id}", postId)
                    .with(csrf()))
                    .andExpect(status().isBadRequest());
        }
    }
}
