package dailymissionproject.demo.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.user.WithMockCustomUser;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.fixture.UserObjectFixture;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Tag("unit")
@DisplayName(("[unit] [controller] UserController"))
@WebMvcTest(UserController.class)
@WithMockCustomUser
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserControllerTest {

    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private final UserDetailResponseDto response = UserObjectFixture.getUserResponse();


    @Test
    @DisplayName("사용자 정보를 조회할 수 있다.")
    void user_detail_test() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User user  = (CustomOAuth2User) authentication.getPrincipal();

        when(userService.detail(any())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/detail"))
                .andExpect(status().isOk())
                .andDo(print());

        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.code").value("200"));
        resultActions.andExpect(jsonPath("$.data.userNickname").value("윤성철"));
    }
}
