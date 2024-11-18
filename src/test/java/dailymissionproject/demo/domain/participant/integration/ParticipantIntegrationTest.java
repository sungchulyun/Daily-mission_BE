package dailymissionproject.demo.domain.participant.integration;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.fixture.ParticipantObjectFixture;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import dailymissionproject.demo.global.config.IntegrationTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@DisplayName("[integration] [controller] ParticipantController")
@ActiveProfiles("test")
@WithMockCustomUser
class ParticipantIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ParticipantRepository participantRepository;

    private CustomOAuth2User oAuth2User;
    private Cookie cookie;
    private ParticipantSaveRequestDto participantSaveRequest;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "USER", "윤성철", "naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA", "윤성철", "https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png", "proattacker@naver.com"));
        cookie = new Cookie("Authorization", jwtUtil.createJwt(oAuth2User.getId(), oAuth2User.getUsername(), "USER", System.currentTimeMillis()));
        participantSaveRequest = ParticipantObjectFixture.getParticipantSaveRequest();
    }

    @Sql(scripts = {
            "/02-init-data.sql"
    })
    @Nested
    @DisplayName("참여 생성 통합 테스트")
    class ParticipantSaveIntegrationTest extends IntegrationTestSupport {

        @Test
        @DisplayName("참여에 성공한다.")
        void test_1() throws Exception {
            Long savedParticipantId = 2L;

            mockMvc.perform(post("/api/v1/participant/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(participantSaveRequest))
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            Participant participant = participantRepository.findById(savedParticipantId).get();

            assertEquals(participantSaveRequest.getMissionId(), participant.getId());
        }
    }
}
