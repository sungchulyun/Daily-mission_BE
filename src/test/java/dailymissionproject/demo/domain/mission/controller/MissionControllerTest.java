package dailymissionproject.demo.domain.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.service.MissionService;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.INVALID_USER_REQUEST;
import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.MISSION_NOT_FOUND;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private final List<MissionUserListResponseDto> missionUserListResponse = MissionObjectFixture.getUserMissionList();

    private final PageResponseDto hotMissionListResponse = MissionObjectFixture.getHotMissionListResponse();
    private final PageResponseDto newMissionListResponse = MissionObjectFixture.getNewMissionListResponse();
    private final PageResponseDto allMissionListResponse = MissionObjectFixture.getAllMissionListResponse();


    private final Pageable pageable = PageRequest.of(0,3 );
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
        @Test
        @DisplayName("미션이 존재하지 않으면 상세 조회에 실패한다.")
        void detail_read_mission_fail_when_mission_not_exits() throws Exception {

            when(missionService.findById(any()))
                    .thenThrow(new MissionException(MISSION_NOT_FOUND));

            mockMvc.perform(get("/api/v1/mission/{missionId}", missionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인기 미션 리스트를 조회할 수 있다.")
        void hot_mission_read_list_success() throws Exception {
            MissionHotListResponseDto hotMission_1 = MissionHotListResponseDto.builder()
                    .id(1L)
                    .title("미션1")
                    .content("열심히 합니다.")
                    .imageUrl("THUMBNAIL1.jpg")
                    .nickname("yoonsu")
                    .startDate(LocalDate.now().minusDays(10))
                    .endDate(LocalDate.now().plusDays(10))
                    .build();

            when(missionService.findHotList(any())).thenReturn(hotMissionListResponse);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/mission/hot")
                    .param("page", String.valueOf(pageable.getPageNumber()))
                    .param("size", String.valueOf(pageable.getPageSize()))
                    .with(csrf())
            )
                    .andExpect(status().isOk())
                    .andDo(print());

            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.code").value(200));
            resultActions.andExpect(jsonPath("$.data[0].id").value(hotMission_1.getId()));
            resultActions.andExpect(jsonPath("$.data[0].title").value(hotMission_1.getTitle()));
            resultActions.andExpect(jsonPath("$.data[0].content").value(hotMission_1.getContent()));
            resultActions.andExpect(jsonPath("$.data[0].imageUrl").value(hotMission_1.getImageUrl()));
            resultActions.andExpect(jsonPath("$.data[0].nickname").value(hotMission_1.getNickname()));
            resultActions.andExpect(jsonPath("$.data[0].startDate").value(hotMission_1.getStartDate().toString()));
            resultActions.andExpect(jsonPath("$.data[0].endDate").value(hotMission_1.getEndDate().toString()));
        }

        @Test
        @DisplayName("신규 미션 리스트를 조회할 수 있다.")
        void new_mission_read_list_success() throws Exception {
            MissionNewListResponseDto newMission_1 = MissionNewListResponseDto.builder()
                    .id(1L)
                    .title("미션1")
                    .content("열심히 합니다.")
                    .imageUrl("THUMBNAIL1.jpg")
                    .nickname("yoonsu")
                    .startDate(LocalDate.now().minusDays(10))
                    .endDate(LocalDate.now().plusDays(10))
                    .build();

            //when
            when(missionService.findNewList(any())).thenReturn(newMissionListResponse);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/mission/new")
                    .param("page", String.valueOf(pageable.getPageNumber()))
                    .param("size", String.valueOf(pageable.getPageSize()))
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            //then
            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.code").value(200));
            resultActions.andExpect(jsonPath("$.data[0].id").value(newMission_1.getId()));
            resultActions.andExpect(jsonPath("$.data[0].title").value(newMission_1.getTitle()));
            resultActions.andExpect(jsonPath("$.data[0].content").value(newMission_1.getContent()));
            resultActions.andExpect(jsonPath("$.data[0].imageUrl").value(newMission_1.getImageUrl()));
            resultActions.andExpect(jsonPath("$.data[0].nickname").value(newMission_1.getNickname()));
            resultActions.andExpect(jsonPath("$.data[0].startDate").value(newMission_1.getStartDate().toString()));
            resultActions.andExpect(jsonPath("$.data[0].endDate").value(newMission_1.getEndDate().toString()));
        }

        @Test
        @DisplayName("전체 미션 리스트를 조회할 수 있다.")
        void all_mission_read_list_success() throws Exception {
            MissionAllListResponseDto allMission_1 = MissionAllListResponseDto.builder()
                    .id(1L)
                    .title("미션1")
                    .content("열심히 합니다.")
                    .imageUrl("THUMBNAIL1.jpg")
                    .nickname("yoonsu")
                    .startDate(LocalDate.now().minusDays(10))
                    .endDate(LocalDate.now().plusDays(10))
                    .build();
            //when
            when(missionService.findAllList(any())).thenReturn(allMissionListResponse);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/mission/all")
                            .param("page", String.valueOf(pageable.getPageNumber()))
                            .param("size", String.valueOf(pageable.getPageSize()))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            //then
            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.code").value(200));
            resultActions.andExpect(jsonPath("$.data[0].id").value(allMission_1.getId()));
            resultActions.andExpect(jsonPath("$.data[0].title").value(allMission_1.getTitle()));
            resultActions.andExpect(jsonPath("$.data[0].content").value(allMission_1.getContent()));
            resultActions.andExpect(jsonPath("$.data[0].imageUrl").value(allMission_1.getImageUrl()));
            resultActions.andExpect(jsonPath("$.data[0].nickname").value(allMission_1.getNickname()));
            resultActions.andExpect(jsonPath("$.data[0].startDate").value(allMission_1.getStartDate().toString()));
            resultActions.andExpect(jsonPath("$.data[0].endDate").value(allMission_1.getEndDate().toString()));
        }

        @Test
        @DisplayName("유저가 참여한 전체 미션 리스트를 조회할 수 있다.")
        void user_mission_read_list_success() throws Exception {

            //when
            when(missionService.findByUserList(any())).thenReturn(missionUserListResponse);

            ResultActions resultActions = mockMvc.perform(get("/api/v1/mission/user")
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            //then
            resultActions.andExpect(jsonPath("$.success").value(true));
            resultActions.andExpect(jsonPath("$.code").value(200));
            resultActions.andExpect(jsonPath("$.data[0].id").value(missionUserListResponse.get(0).getId()));
            resultActions.andExpect(jsonPath("$.data[0].title").value(missionUserListResponse.get(0).getTitle()));
            resultActions.andExpect(jsonPath("$.data[0].content").value(missionUserListResponse.get(0).getContent()));
            resultActions.andExpect(jsonPath("$.data[0].imageUrl").value(missionUserListResponse.get(0).getImageUrl()));
            resultActions.andExpect(jsonPath("$.data[0].nickname").value(missionUserListResponse.get(0).getNickname()));
            resultActions.andExpect(jsonPath("$.data[0].startDate").value(missionUserListResponse.get(0).getStartDate().toString()));
            resultActions.andExpect(jsonPath("$.data[0].endDate").value(missionUserListResponse.get(0).getEndDate().toString()));
            resultActions.andExpect(jsonPath("$.data[0].ended").value(missionUserListResponse.get(0).isEnded()));
        }

        @Test
        @DisplayName("유저 정보가 없을 경우 예외를 반환한다.")
        void user_mission_read_list_fail() throws Exception {

            when(missionService.findByUserList(any())).thenThrow(new UserException(USER_NOT_FOUND));

            mockMvc.perform(get("/api/v1/mission/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("미션 생성 컨트롤러 테스트")
    class MissionSaveControllerTest {

        @Test
        @DisplayName("미션을 생성할 수 있다.")
        void save_mission_test() throws Exception {

            final String fileName = "https://AWS-s3/missionThumbnail.jpg";
            final String contentType = "image/jpeg";

            MockMultipartFile file = new MockMultipartFile("file", fileName, contentType, "test data".getBytes(StandardCharsets.UTF_8));
            MockMultipartFile request = new MockMultipartFile("missionReqDto", "request.json", "application/json", objectMapper.writeValueAsBytes(missionSaveRequest));

            //given
            when(missionService.save(any(), eq(missionSaveRequest), eq(file))).thenReturn(missionSaveResponse);

            //when
            mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/mission/save")
                    .file(file)
                    .file(request)
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            //then
            verify(missionService, description("save 메서드가 정상적으로 호출됨"))
                    .save(any(), any(MissionSaveRequestDto.class), eq(file));
        }
    }

    @Nested
    @DisplayName("미션 수정 컨트롤러 테스트")
    class MissionUpdateControllerTest {

        @Test
        @DisplayName("미션을 수정할 수 있다.")
        void update_mission_success() throws Exception {

            when(missionService.update(eq(missionId), any(), eq(missionUpdateRequest))).thenReturn(missionUpdateResponse);

            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(missionUpdateRequest))
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print());

            verify(missionService, description("update 메서드가 정상적으로 호출됨"))
                    .update(anyLong(), any(), any(MissionUpdateRequestDto.class));
        }

        @Test
        @DisplayName("미션이 존재하지 않으면 수정할 수 없다.")
        void update_mission_fail_when_mission_not_exist() throws Exception {

            when(missionService.update(any(),any(CustomOAuth2User.class), any(MissionUpdateRequestDto.class)))
                    .thenThrow(new MissionException(MISSION_NOT_FOUND));

            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(missionUpdateRequest))
                    .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andDo(print());

            verify(missionService, description("update 메서드가 정상적으로 호출됨"))
                    .update(anyLong(), any(), any(MissionUpdateRequestDto.class));
        }

        @Test
        @DisplayName("방장이 아니라면 미션을 수정할 수 없다.")
        void update_mission_fail_when_user_is_not_host() throws Exception {

            when(missionService.update(any(), any(CustomOAuth2User.class), any(MissionUpdateRequestDto.class)))
                    .thenThrow(new MissionException(INVALID_USER_REQUEST));

            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(missionUpdateRequest))
                    .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andDo(print());

           verify(missionService, description("isUser 메서드가 호출됨"))
                   .update(any(),any(CustomOAuth2User.class), any(MissionUpdateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("미션 삭제 컨트롤러 테스트")
    class MissionDeleteControllerTest {

        @Test
        @DisplayName("미션을 삭제할 수 있다.")
        void delete_mission_success() throws Exception {

            when(missionService.delete(anyLong(), any(CustomOAuth2User.class))).thenReturn(true);

            mockMvc.perform(delete("/api/v1/mission/{missionId}", missionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf()))
                    .andExpect(status().isOk());

            verify(missionService, description("delete 메서드가 정상적으로 호출됨"))
                    .delete(anyLong(), any(CustomOAuth2User.class));
        }

        @Test
        @DisplayName("미션이 존재하지 않으면 삭제할 수 없다.")
        void delete_mission_fail_when_mission_not_exist() throws Exception {

            when(missionService.update(any(),any(CustomOAuth2User.class), any(MissionUpdateRequestDto.class)))
                    .thenThrow(new MissionException(MISSION_NOT_FOUND));

            mockMvc.perform(put("/api/v1/mission/{missionId}", missionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(missionUpdateRequest))
                            .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andDo(print());

            verify(missionService, description("update 메서드가 정상적으로 호출됨"))
                    .update(anyLong(), any(), any(MissionUpdateRequestDto.class));
        }
    }
}


