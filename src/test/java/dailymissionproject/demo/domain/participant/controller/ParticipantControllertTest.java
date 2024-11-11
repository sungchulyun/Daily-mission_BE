package dailymissionproject.demo.domain.participant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.exception.ParticipantException;
import dailymissionproject.demo.domain.participant.exception.ParticipantExceptionCode;
import dailymissionproject.demo.domain.participant.fixture.ParticipantObjectFixture;
import dailymissionproject.demo.domain.participant.service.ParticipantService;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Tag("unit")
@DisplayName("[unit] [controller] ParticipantController")
@WebMvcTest(controllers = ParticipantController.class)
@WithMockCustomUser
class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ParticipantService participantService;

    private CustomOAuth2User oAuth2User;
    private final ParticipantSaveRequestDto participantSaveRequest = ParticipantObjectFixture.getParticipantSaveRequest();
    private final boolean isParticipantAble = true;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google 1923819273", "sungchul", "aws-s3", "google@gmailcom"));
    }

    @Nested
    @DisplayName("참여 생성 컨트롤러 테스트")
    class ParticipantJoinControllerTest {
        @Test
        @DisplayName("미션에 참여할 수 있다.")
        void participant_mission_success() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenReturn(isParticipantAble);

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isOk());

            verify(participantService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(), any(ParticipantSaveRequestDto.class));
        }

        @Test
        @DisplayName("미션이 존재하지 않으면 참여에 실패한다.")
        void participant_mission_fail_when_mission_is_not_exits() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenThrow(new MissionException(MissionExceptionCode.MISSION_NOT_FOUND));

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("유저 정보가 없으면 참여에 실패한다.")
        void participant_mission_fail_when_user_is_not_exits() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenThrow(new UserException(UserExceptionCode.USER_NOT_FOUND));

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("강제퇴장 이력이 있으면 참여에 실패한다.")
        void participant_mission_fail_when_user_is_banned() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenThrow(new ParticipantException(ParticipantExceptionCode.BAN_HISTORY_EXISTS));

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("이미 참여중이면 참여에 실패한다.")
        void participant_mission_fail_when_user_already_join() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenThrow(new ParticipantException(ParticipantExceptionCode.PARTICIPANT_ALREADY_EXISTS));

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("credential이 틀리면 참여에 실패한다.")
        void participant_mission_fail_when_credential_is_not_correct() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenThrow(new ParticipantException(ParticipantExceptionCode.INVALID_INPUT_VALUE_CREDENTIAL));

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("참여불가한 미션이면 참여에 실패한다.")
        void participant_mission_fail_when_mission_is_not_allowed_to_join() throws Exception {
            when(participantService.save(any(), any(ParticipantSaveRequestDto.class))).thenThrow(new ParticipantException(ParticipantExceptionCode.INVALID_PARTICIPATE_REQUEST));

            mockMvc.perform(post("/api/v1/participant/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(participantSaveRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }
    }


}
