package dailymissionproject.demo.domain.mission.integration;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionDetailResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionSaveResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionUpdateResponseDto;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.global.WithMockCustomUser;
import dailymissionproject.demo.global.config.IntegrationTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private Long missionId = 1L;
    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "USER", "윤성철", "naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA", "윤성철", "https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png", "proattacker@naver.com"));
        cookie = new Cookie("Authorization", jwtUtil.createJwt(oAuth2User.getId(), oAuth2User.getUsername(), "USER", System.currentTimeMillis()));
        missionSaveRequest = MissionObjectFixture.getMissionSaveRequest();
        missionSaveResponse = MissionObjectFixture.getMissionSaveResponse();
        missionDetailResponse = MissionObjectFixture.getMissionDetailResponse();
        missionUpdateRequest = MissionObjectFixture.getMissionUpdateRequest();
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
}
