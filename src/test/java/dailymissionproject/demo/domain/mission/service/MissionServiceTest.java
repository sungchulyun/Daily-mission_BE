package dailymissionproject.demo.domain.mission.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.exception.MissionExceptionCode;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.*;
import static dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture.getMissionList;
import static dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture.getUserMissionList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("[unit] [service] MissionService")
@ExtendWith(MockitoExtension.class)
@WithMockUser
class MissionServiceTest {
    @InjectMocks
    private MissionService missionService;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ParticipantRepository participantRepository;


    private final Mission mission = MissionObjectFixture.getMissionFixture();
    private final User user = MissionObjectFixture.getUserFixture();
    private final MissionRule missionRule = MissionObjectFixture.getMissionRuleFixture();
    private final MissionRuleResponseDto missionRuleResponse = MissionObjectFixture.getMissionRuleResponseFixture();
    private final List<ParticipantUserDto> participantUserList = MissionObjectFixture.getParticipantUserFixture();

    private final MissionSaveRequestDto missionSaveRequest = MissionObjectFixture.getMissionSaveRequest();
    private final MissionSaveResponseDto missionSaveResponse = MissionObjectFixture.getMissionSaveResponse();

    private final MissionDetailResponseDto missionDetailResponse = MissionObjectFixture.getMissionDetailResponse();

    private final MissionUpdateRequestDto missionUpdateRequest = MissionObjectFixture.getMissionUpdateRequest();
    private final MissionUpdateResponseDto missionUpdateResponse = MissionObjectFixture.getMissionUpdateResponse();

    private CustomOAuth2User oAuth2User;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        oAuth2User = CustomOAuth2User.create(new UserDto(1L, "ROLE_USER", "윤성철", "google 1923819273", "sungchul", "aws-s3", "google@gmailcom"));
        pageable = PageRequest.of(0, 3);
    }
        @Nested
        @DisplayName("미션 생성 서비스 레이어 테스트")
        class MissionSaveServiceTest {
            final String fileName = "userModifyImage";
            final String contentType = "image/jpeg";

            @DisplayName("미션을 생성할 수 있다.")
            @Test
            void mission_save_success() throws IOException {
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(participantRepository.save(MissionObjectFixture.getParticipant())).thenReturn(MissionObjectFixture.getParticipant());

                //when
                MissionSaveResponseDto missionSaveResponse = missionService.save(oAuth2User, missionSaveRequest);

                //then
                assertEquals(missionSaveResponse.getCredential(), mission.getCredential());
            }

            @DisplayName("미션 시작일자가 오늘보다 이전 날짜라면 미션을 생성할 수 없다.")
            @Test
            void mission_save_fail_when_start_date_is_before_than_today() throws IOException {
                MockMultipartFile file = new MockMultipartFile("file", "test data".getBytes(StandardCharsets.UTF_8));

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

                MissionSaveRequestDto missionSaveRequestDto = MissionSaveRequestDto.builder()
                        .title("TITLE")
                        .content("CONTENT")
                        .hint("HINT")
                        .credential("CREDENTIAL")
                        .startDate(LocalDate.now().minusDays(1))
                        .endDate(LocalDate.now().plusMonths(1))
                        .week(new Week(false, true, true, true, true, true, false))
                        .build();

                assertThrows(MissionException.class, () -> missionService.save(oAuth2User, missionSaveRequestDto));
            }

            @DisplayName("미션 시작일자가 미션 종료일자보다 이전 날짜라면 미션을 생성할 수 없다.")
            @Test
            void mission_save_fail_when_start_date_is_after_end_date() throws IOException {
                MockMultipartFile file = new MockMultipartFile("file", "test data".getBytes(StandardCharsets.UTF_8));

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

                MissionSaveRequestDto missionSaveRequestDto = MissionSaveRequestDto.builder()
                        .title("TITLE")
                        .content("CONTENT")
                        .hint("HINT")
                        .credential("CREDENTIAL")
                        .startDate(LocalDate.now().plusDays(10))
                        .endDate(LocalDate.now().plusDays(5))
                        .week(new Week(false, true, true, true, true, true, false))
                        .build();

                assertThrows(MissionException.class, () -> missionService.save(oAuth2User, missionSaveRequestDto));
            }
        }

        @Nested
        @DisplayName("미션 조회 서비스 레이어 테스트")
        class MissionReadServiceTest {

            @Test
            @DisplayName("미션을 상세조회 할 수 있다.")
            void test_mission_detail_read() {
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission));

                MissionDetailResponseDto missionDetailResponse = missionService.findById(any());

                //then
                assertEquals(mission.getTitle(), missionDetailResponse.getTitle());
            }

            @Test
            @DisplayName("미션이 존재하지 않거나 삭제되었으면 조회할 수 없다.")
            void test_mission_detail_read_fail_when_deleted_or_not_exist() {
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenThrow(new MissionException(MissionExceptionCode.MISSION_NOT_FOUND));

                assertThrows(MissionException.class, () -> missionService.findById(any()));
            }

            @Test
            @DisplayName("인기 미션 리스트를 조회할 수 있다.")
            void test_mission_read_hot_list_success() {
                when(missionRepository.findAllByParticipantSize(pageable, 1L)).thenReturn(MissionObjectFixture.getHotMissionListPageable());

                PageResponseDto pageResponseDto = missionService.findHotList(pageable, oAuth2User);
                List<MissionHotListResponseDto> list = (List<MissionHotListResponseDto>) pageResponseDto.content();

                assertEquals(list.get(0).getTitle(),
                        MissionObjectFixture.getHotMissionListPageable().getContent().get(0).getTitle());
            }

            @Test
            @DisplayName("신규 미션 리스트를 조회할 수 있다.")
            void test_mission_read_new_list_success() {
                when(missionRepository.findAllByCreatedInMonth(pageable, 1L)).thenReturn(MissionObjectFixture.getNewMissionListPageable());

                PageResponseDto pageResponseDto = missionService.findNewList(pageable, oAuth2User);
                List<MissionNewListResponseDto> list = (List<MissionNewListResponseDto>) pageResponseDto.content();

                assertEquals(list.get(0).getTitle(),
                        MissionObjectFixture.getNewMissionListPageable().getContent().get(0).getTitle());
            }

            @Test
            @DisplayName("전체 미션 리스트를 조회할 수 있다.")
            void test_mission_read_all_list_success() {
                when(missionRepository.findAllByCreatedDate(pageable, 1L)).thenReturn(MissionObjectFixture.getAllMissionListPageable());

                PageResponseDto pageResponseDto = missionService.findAllList(pageable, oAuth2User);
                List<MissionAllListResponseDto> list = (List<MissionAllListResponseDto>) pageResponseDto.content();

                assertEquals(list.get(0).getTitle(),
                        MissionObjectFixture.getAllMissionListPageable().getContent().get(0).getTitle());
            }


            @Test
            @DisplayName("유저가 참여중인 미션 리스트를 조회할 수 있다.")
            void test_mission_read_user_joining_list_success(){
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(missionRepository.findAll()).thenReturn(getMissionList());

                boolean result = missionService.isParticipating(getUserMissionList());
                List<MissionUserListResponseDto> userMissionResponse = missionService.findByUserList(oAuth2User);
                assertTrue(result);
                assertEquals(userMissionResponse.get(0).getTitle(), getMissionList().get(0).getTitle());
            }

        }

        @Nested
        @DisplayName("미션 수정 서비스 레이어 테스트")
        class MissionModifyServiceTest {

            @Test
            @DisplayName("미션을 수정할 수 있다.")
            void mission_modify_success() throws IOException {
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission));

                MissionUpdateResponseDto updateResponse = missionService.update(1L, oAuth2User, missionUpdateRequest);

                assertEquals(updateResponse.getCredential(), missionUpdateResponse.getCredential());
                assertEquals(updateResponse.getHint(), missionUpdateResponse.getHint());
            }


            @Test
            @DisplayName("방장이 아니라면 미션을 수정할 수 없다.")
            void mission_modify_fail_when_request_user_is_not_host() {
                //given
                User guestUser = new User(200L, "name", "email", "nickname");

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(guestUser));
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission));

                //when
                MissionException missionException = assertThrows(MissionException.class, () ->
                        missionService.update(1L, oAuth2User, missionUpdateRequest));

                //then
                assertEquals(INVALID_USER_REQUEST, missionException.getExceptionCode());
            }
        }

        @Nested
        @DisplayName("미션 삭제 서비스 레이어 테스트")
        class MissionDeleteServiceTest {

            @Test
            @DisplayName("미션을 삭제할 수 있다.")
            void mission_delete_success() throws IOException {
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission));

                boolean result = missionService.delete(1L, oAuth2User);
                assertTrue(result);
            }

            @Test
            @DisplayName("방장이 아니면 미션을 삭제할 수 없다.")
            void mission_delete_fail_when_request_user_is_not_host() {
                User guestUser = new User(200L, "name", "email", "nickname");

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(guestUser));
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission));

                MissionException missionException = assertThrows(MissionException.class, () ->
                        missionService.delete(1L, oAuth2User));

                //then
                assertEquals(INVALID_USER_REQUEST, missionException.getExceptionCode());
            }

            @Test
            @DisplayName("이미 삭제된 미션은 삭제할 수 없다.")
            void mission_delete_fail_when_mission_is_already_delete() {
                //given
               Mission mission_1 = new Mission(100L, user, "title", "content", "image"
                       , LocalDate.now(), LocalDate.now());
               mission_1.delete();

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission_1));

                MissionException missionException = assertThrows(MissionException.class, () ->
                        missionService.delete(mission_1.getId(), oAuth2User));

                assertEquals(MISSION_ALREADY_DELETED, missionException.getExceptionCode());
            }

            @Test
            @DisplayName("방장을 제외하고 참여중인 유저가 있다면 삭제할 수 없다.")
            void mission_delete_fail_when_mission_participating_user_exists(){
                //given
                Mission mission_1 = new Mission(100L, user, "title", "content", "image"
                        , LocalDate.now(), LocalDate.now());

                Participant participant_1 = Participant.builder()
                        .mission(mission_1)
                        .user(user)
                        .build();

                Participant participant_2 = Participant.builder()
                        .mission(mission_1)
                        .user(new User(100L, "name", "email", "nickname"))
                        .build();

                mission_1.setParticipants(List.of(participant_1, participant_2));

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(missionRepository.findByIdAndDeletedIsFalse(any())).thenReturn(Optional.of(mission_1));

                //when
                MissionException missionException = assertThrows(MissionException.class, () ->
                        missionService.delete(mission_1.getId(), oAuth2User));

                assertEquals(INVALID_DELETE_REQUEST, missionException.getExceptionCode());
            }
        }
    }
