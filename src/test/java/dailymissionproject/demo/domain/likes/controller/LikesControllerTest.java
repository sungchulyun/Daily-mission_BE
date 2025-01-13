package dailymissionproject.demo.domain.likes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.like.controller.LikesController;
import dailymissionproject.demo.domain.like.dto.response.LikesResponseDto;
import dailymissionproject.demo.domain.like.service.LikesService;
import dailymissionproject.demo.domain.likes.fixture.LikesObjectFixture;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Tag("unit")
@DisplayName("[unit] [controller] LikesController")
@WebMvcTest(controllers = LikesController.class)
@WithMockCustomUser
public class LikesControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LikesService likesService;

    private final Post post = LikesObjectFixture.getPostFixture();
    private final User user = LikesObjectFixture.getUserFixture();
    private final LikesResponseDto response = LikesObjectFixture.getLikeResponse();

    public static CustomOAuth2User oAuth2User;
    private final Long userId = 1L;
    private final Long postId = 1L;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google 1923819273", "sungchul", "aws-s3", "google@gmailcom"));
    }

    @Test
    @DisplayName("좋아요를 추가할 수 있다.")
    void likes_add_success() throws Exception {

    }
}
