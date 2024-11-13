package dailymissionproject.demo.domain.user.integration;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.auth.jwt.JWTUtil;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.fixture.UserObjectFixture;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import dailymissionproject.demo.global.config.IntegrationTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.NICKNAME_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@DisplayName("[integration][controller] UserController")
@WithMockCustomUser
class UserIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    private UserUpdateRequestDto userUpdateRequest;
    private UserUpdateRequestDto duplicateNicknameUpdateRequest;
    private UserUpdateResponseDto userUpdateResponse;
    private CustomOAuth2User oAuth2User;
    private Cookie cookie;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "USER", "윤성철", "naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA", "윤성철", "https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png", "proattacker@naver.com"));
        userUpdateRequest = UserObjectFixture.getUserUpdateRequest();
        duplicateNicknameUpdateRequest = UserObjectFixture.getDuplicateNicknameUpdateRequest();
        userUpdateResponse = UserObjectFixture.getUserUpdateResponse();
        jwtToken = jwtUtil.createJwt(oAuth2User.getId(), oAuth2User.getUsername(), "USER", System.currentTimeMillis());
        cookie = new Cookie("Authorization", jwtToken);
    }
    @Sql(scripts = {
            "/02-init-data.sql"}
    )

    @Nested
    @DisplayName("[Integration] 유저 조회 통합테스트")
    class UserReadIntegrationTest extends IntegrationTestSupport {

        @Test
        @DisplayName("유저 정보를 조회할 수 있다.")
        void test_1() throws Exception{
            MvcResult result = mockMvc.perform(get("/api/v1/user/detail")
                            .cookie(cookie)
                            .with(csrf())
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            String responseString =result.getResponse().getContentAsString(StandardCharsets.UTF_8);
            GlobalResponse response = objectMapper.readValue(responseString, GlobalResponse.class);
            log.info("response : {}", response.getData());
        }
    }

    @Sql(scripts = {
            "/02-init-data.sql"}
    )
    
    @Nested
    @DisplayName("[Integration] 유저 수정 통합테스트")
    class UserUpdateIntegrationTest extends IntegrationTestSupport {

        @Test
        @DisplayName("유저 정보를 수정할 수 있다.")
        void test_1() throws Exception {

            final Long userId = oAuth2User.getId();
            User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

            mockMvc.perform(put("/api/v1/user/profile", oAuth2User)

                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpdateRequest))
                    .cookie(cookie)
                    .with(csrf())
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            User savedUser = userRepository.findById(userId).orElseGet(null);

            assertEquals(savedUser.getNickname(), userUpdateResponse.getNickname());
        }

        @Test
        @DisplayName("이미 사용중인 닉네임이면 수정에 실패한다.")
        void test_2() throws Exception {
            UserException userException = new UserException(NICKNAME_ALREADY_EXISTS);

            mockMvc.perform(put("/api/v1/user/profile", oAuth2User)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(duplicateNicknameUpdateRequest))
                    .with(csrf())
                    .cookie(cookie))
                    .andDo(print())
                    .andExpect(status().isBadRequest()).andDo(print())
                    .andExpect(jsonPath("$.errors.message").value(userException.getMessage()))
                    .andReturn();
        }
    }
}
