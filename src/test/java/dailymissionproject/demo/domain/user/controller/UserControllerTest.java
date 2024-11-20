package dailymissionproject.demo.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.fixture.UserObjectFixture;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import dailymissionproject.demo.domain.user.service.UserService;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@DisplayName(("[unit] [controller] UserController"))
@WebMvcTest(UserController.class)
@WithMockCustomUser
class UserControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private final UserDetailResponseDto detailResponse = UserObjectFixture.getUserResponse();
    private final UserUpdateRequestDto updateRequest = UserObjectFixture.getUserUpdateRequest();
    private final UserUpdateResponseDto updateResponse = UserObjectFixture.getUserUpdateResponse();


    @Test
    @DisplayName("유저 정보를 조회할 수 있다.")
    void getUser_detail_test() throws Exception {

        when(userService.detail(any())).thenReturn(detailResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/detail"))
                .andExpect(status().isOk())
                .andDo(print());

        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.code").value("200"));
        resultActions.andExpect(jsonPath("$.data.nickname").value(detailResponse.getNickname()));
    }

    @Test
    @DisplayName("유저 정보 업데이트 할 수 있다.")
    void update_userDetail_test() throws Exception {
        //given
        when(userService.updateProfile(any(), eq(updateRequest))).thenReturn(updateResponse);

        //when
        mockMvc.perform(put("/api/v1/user/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        verify(userService, description("updateProfile 메서드가 정상적으로 호출됨"))
                .updateProfile(any(), any(UserUpdateRequestDto.class));
    }
}
