package dailymissionproject.demo.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.post.service.PostService;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    private final PostResponseDto detailResponse = PostObjectFixture.getDetailResponse();

    private static CustomOAuth2User oAuth2User;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google 1923819273", "sungchul", "aws-s3", "google@gmailcom"));
    }

    @Nested
    @DisplayName("포스트 생성 컨트롤러 테스트")
    class PostSaveControllerTest {
        final String fileName = "https://AWS-S3/postThumbnail.jpg";
        final String contentType = "image/jpeg";

        @Test
        @DisplayName("포스트를 생성 할 수 있다.")
        void post_save_success() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", fileName, contentType
                    , "test data".getBytes(StandardCharsets.UTF_8));
            MockMultipartFile request = new MockMultipartFile("postSaveReqDto", "request.json",
                    "application/json", objectMapper.writeValueAsBytes(saveRequest));

            when(postService.save(any(CustomOAuth2User.class), eq(saveRequest), eq(file))).thenReturn(1L);

            mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/post/save")
                    .file(file)
                    .file(request)
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            verify(postService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(), any(PostSaveRequestDto.class), any());
        }

        @Test
        @DisplayName("포스트 생성에 실패한다.")
        void post_save_fail() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", fileName, contentType
                    , "test data".getBytes(StandardCharsets.UTF_8));
            MockMultipartFile request = new MockMultipartFile("postSaveReqDto", "request.json", "application/json"
            , objectMapper.writeValueAsBytes(saveRequest));

            when(postService.save(any(), any(), any())).thenThrow(new MissionException(MissionExceptionCode.MISSION_NOT_FOUND));

            mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/post/save")
                    .file(file)
                    .file(request)
                    .with(csrf()))
                    .andExpect(status().isBadRequest());

            verify(postService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(CustomOAuth2User.class), any(), any());
        }
    }


}
