package dailymissionproject.demo.domain.mission.integration;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionDetailResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionSaveResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionUpdateResponseDto;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
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
@DisplayName("[integration] [controller] MissionController")
@WithMockCustomUser
class MissionIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MissionRepository missionRepository;

    private CustomOAuth2User oAuth2User;
    private Cookie cookie;
    private MissionSaveRequestDto missionSaveRequest;
    private MissionSaveResponseDto missionSaveResponse;
    private MissionDetailResponseDto missionDetailResponse;
    private MissionUpdateRequestDto missionUpdateRequest;
    private MissionUpdateResponseDto missionUpdateResponse;
    private MissionUpdateRequestDto nullUpdateRequest;
    private MissionUpdateRequestDto hintUpdateRequest;
    private Long missionId = 1L;
    private Long savedMissionId = 2L;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "USER", "윤성철", "naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA", "윤성철", "https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png", "proattacker@naver.com"));
        cookie = new Cookie("Authorization", jwtUtil.createJwt(oAuth2User.getId(), oAuth2User.getUsername(), "USER", System.currentTimeMillis()));
        missionSaveRequest = MissionObjectFixture.getMissionSaveRequest();
        missionSaveResponse = MissionObjectFixture.getMissionSaveResponse();
        missionDetailResponse = MissionObjectFixture.getMissionDetailResponse();
        missionUpdateRequest = MissionObjectFixture.getMissionUpdateRequest();
        nullUpdateRequest = MissionObjectFixture.getNullUpdateRequest();
        hintUpdateRequest = MissionObjectFixture.getHintUpdateRequest();
        missionUpdateResponse = MissionObjectFixture.getMissionUpdateResponse();
    }

    @Sql(scripts = {
            "/02-init-data.sql"
    })
    @Nested
    @DisplayName("[Integration] 미션 조회 통합테스트")
    class MissionReadIntegrationTest extends IntegrationTestSupport {

        @Test
        @DisplayName("미션 상세 조회에 성공한다.")
        void test_1() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/v1/mission/{missionId}", missionId)
                            .cookie(cookie)
                            .with(csrf())
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            String responseString = result.getResponse().getContentAsString();
            GlobalResponse globalResponse = objectMapper.readValue(responseString, GlobalResponse.class);
            log.info("response : {}", globalResponse.getData());
        }

        @Test
        @DisplayName("유저별 미션 리스트 조회에 성공한다.")
        void test_2() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/v1/mission/user")
                            .with(csrf())
                            .cookie(cookie)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            String responseString = result.getResponse().getContentAsString();
            GlobalResponse globalResponse = objectMapper.readValue(responseString, GlobalResponse.class);
        }
    }

    @Sql(scripts = {
            "/02-init-data.sql"
    })
    @Nested
    @DisplayName("[Integration] 미션 생성 통합 테스트")
    class MissionSaveIntegrationTest {

        @Test
        @DisplayName("미션 생성에 성공한다.")
        void test1() throws Exception {
            mockMvc.perform(post("/api/v1/mission/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(missionSaveRequest))
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            MvcResult afterSave = mockMvc.perform(get("/api/v1/mission/{missionId}", savedMissionId)
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            String responseString2 = afterSave.getResponse().getContentAsString(StandardCharsets.UTF_8);
            GlobalResponse response2 = objectMapper.readValue(responseString2, GlobalResponse.class);
            MissionDetailResponseDto missionDetailResponse2 = objectMapper.convertValue(response2.getData(), MissionDetailResponseDto.class);

            assertEquals(1, missionDetailResponse2.getParticipantDto().size());
            assertEquals(missionDetailResponse2.getNickname(), oAuth2User.getNickname());
        }
    }

    @Sql(scripts = {
            "/02-init-data.sql"
    })

    @Nested
    @DisplayName("[Integration] 미션 수정 통합 테스트")
    class MissionUpdateIntegrationTest {

        @Test
        @DisplayName("미션을 수정할 수 있다.")
        void test_1() throws Exception {
            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(missionUpdateRequest))
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

           Mission savedMission = missionRepository.findByIdAndDeletedIsFalse(missionId).get();

            assertEquals(savedMission.getCredential(), missionUpdateRequest.getCredential());
        }

        @Test
        @DisplayName("request의 모든 필드가 null이면 미션 수정에 실패한다.")
        void test_2() throws Exception {
            MissionException missionException = new MissionException(MissionExceptionCode.UPDATE_INPUT_IS_EMPTY);

            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(nullUpdateRequest))
                    .with(csrf())
                    .cookie(cookie)
            )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.message").value(missionException.getExceptionCode().getMessage()));
        }

        @Test
        @DisplayName("request의 하나의 필드에만 null 값이 아니면 수정에 성공한다.")
        void test_3() throws Exception {
            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(hintUpdateRequest))
                    .with(csrf())
                    .cookie(cookie))
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            Mission savedMission = missionRepository.findByIdAndDeletedIsFalse(missionId).get();

            assertEquals(savedMission.getHint(), hintUpdateRequest.getHint());
        }
    }
}
