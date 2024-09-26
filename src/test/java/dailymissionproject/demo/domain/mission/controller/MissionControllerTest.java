package dailymissionproject.demo.domain.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionDetailResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionSaveResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionUpdateResponseDto;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.service.MissionService;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Tag("unit")
@DisplayName("[unit] [controller] MissionController")
@WebMvcTest(MissionController.class)
@WithMockCustomUser
class MissionControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private MissionService missionService;

    private final Mission getMissionFixture = MissionObjectFixture.getMissionFixture();
    private final User getUserFixture = MissionObjectFixture.getUserFixture();
    private final List<ParticipantUserDto> getParticipantUserFixture = MissionObjectFixture.getParticipantUserFixture();
    private final MissionRuleResponseDto getMissionRuleResponse = MissionObjectFixture.getMissionRuleResponseFixture();

    private final MissionSaveRequestDto missionSaveRequest = MissionObjectFixture.getMissionSaveRequest();
    private final MissionSaveResponseDto missionSaveResponse = MissionObjectFixture.getMissionSaveResponse();

    private final MissionDetailResponseDto missionDetailResponse = MissionObjectFixture.getMissionDetailResponse();

    private final MissionUpdateRequestDto missionUpdateRequest = MissionObjectFixture.getMissionUpdateRequest();
    private final MissionUpdateResponseDto missionUpdateResponse = MissionObjectFixture.getMissionUpdateResponse();

    private final PageResponseDto hotMissionListResponse = MissionObjectFixture.getHotMissionListResponse();
    private final PageResponseDto newMissionListResponse = MissionObjectFixture.getNewMissionListResponse();
    private final PageResponseDto allMissionListResponse = MissionObjectFixture.getAllMissionListResponse();

    private final Long missionId = 1L;

    @Nested
    @DisplayName("미션 조회 컨트롤러 테스트")
    class MissionReadControllerTest {

        @Test
        @DisplayName("미션 상세정보를 조회할 수 있다.")
        void detail_read_mission_success() throws Exception {
            //given

            //when
           when(missionService.findById(any())).thenReturn(missionDetailResponse);

           ResultActions resultActions = mockMvc.perform(get("/api/v1/mission/{missionId}", missionId)
                           .contentType(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andDo(print());

           //then
            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.code").value(200));
            resultActions.andExpect(jsonPath("$.data.title").value(missionDetailResponse.getTitle()));
            resultActions.andExpect(jsonPath("$.data.content").value(missionDetailResponse.getContent()));
            resultActions.andExpect(jsonPath("$.data.imageUrl").value(missionDetailResponse.getImageUrl()));
            resultActions.andExpect(jsonPath("$.data.hint").value(missionDetailResponse.getHint()));
            resultActions.andExpect(jsonPath("$.data.nickname").value(missionDetailResponse.getNickname()));
            resultActions.andExpect(jsonPath("$.data.startDate").value(missionDetailResponse.getStartDate().toString()));
            resultActions.andExpect(jsonPath("$.data.endDate").value(missionDetailResponse.getEndDate().toString()));
            resultActions.andExpect(jsonPath("$.data.missionRuleResponseDto.week.sun").value(false));
            resultActions.andExpect(jsonPath("$.data.participantDto[0].id").value(missionId));
        }
    }


}
