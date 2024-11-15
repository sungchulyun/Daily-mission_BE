package dailymissionproject.demo.domain.post.integration;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostDetailResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostSaveResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUpdateResponseDto;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import dailymissionproject.demo.global.config.IntegrationTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@DisplayName("[integration] [controller] PostController")
@WithMockCustomUser
class PostIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private PostRepository postRepository;

    private CustomOAuth2User oAuth2User;
    private Cookie cookie;
    private Long postId = 1L;
    private Long postSavedId = 2L;
    private PostSaveRequestDto postSaveRequest;
    private PostSaveResponseDto postSaveResponse;
    private PostUpdateRequestDto postUpdateRequest;
    private PostUpdateResponseDto postUpdateResponse;

    @BeforeEach
    void setUp() throws Exception {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "USER", "윤성철", "naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA", "윤성철", "https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png", "proattacker@naver.com"));
        cookie = new Cookie("Authorization", jwtUtil.createJwt(oAuth2User.getId(), oAuth2User.getUsername(), "USER", System.currentTimeMillis()));
        postSaveRequest = PostObjectFixture.getSaveRequest();
        postSaveResponse = PostObjectFixture.getSaveResponse();
        postUpdateRequest = PostObjectFixture.getPostUpdateRequest();
        postUpdateResponse = PostObjectFixture.getPostUpdateResponse();
    }

    @Sql(scripts = {
            "/02-init-data.sql"
    })
    @Nested
    @DisplayName("[Integration] 포스트 생성 통합테스트")
    class PostSaveIntegrationTest {

        @Test
        @DisplayName("포스트 생성에 성공한다.")
        void test_1() throws Exception {
            mockMvc.perform(post("/api/v1/post/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postSaveRequest))
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            MvcResult afterSave = mockMvc.perform(get("/api/v1/post/{postSavedId}", postSavedId)
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            String responseString = afterSave.getResponse().getContentAsString(StandardCharsets.UTF_8);
            GlobalResponse response = objectMapper.readValue(responseString, GlobalResponse.class);
            PostDetailResponseDto afterResponse = objectMapper.convertValue(response.getData(), PostDetailResponseDto.class);

            assertEquals(postSaveRequest.getMissionId(), afterResponse.getMissionId());
        }
    }

    @Sql(scripts = {
            "/02-init-data.sql"
    })
    @Nested
    @DisplayName("포스트 수정 통합 테스트")
    class PostUpdateIntegrationTest {

        @Test
        @DisplayName("포스트 수정에 성공한다.")
        void test_1() throws Exception {
            mockMvc.perform(put("/api/v1/post/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postUpdateRequest))
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            Post savedPost = postRepository.findById(postId).get();

            assertEquals(postUpdateRequest.getTitle(), savedPost.getTitle());
        }
    }
}
