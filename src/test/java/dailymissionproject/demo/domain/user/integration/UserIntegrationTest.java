package dailymissionproject.demo.domain.user.integration;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.fixture.UserObjectFixture;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import dailymissionproject.demo.global.config.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@DisplayName("[integartion][controller] UserController")
@WithMockCustomUser
class UserIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    private UserUpdateRequestDto userUpdateRequest;

    @BeforeEach
    void setUp() {
        userUpdateRequest = UserObjectFixture.getUserUpdateRequest();
    }

    @Test
    @DisplayName("유저 정보를 조회할 수 있다.")
    void test_1() throws Exception{
        MvcResult result = mockMvc.perform(get("/api/v1/user/detail")
                .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        String responseString =result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        GlobalResponse response = objectMapper.readValue(responseString, GlobalResponse.class);
        log.info("response : {}", response);
    }

    @Test
    @DisplayName("유저 정보를 수정할 수 있다.")
    void update_user_success() throws Exception {

    }
}
