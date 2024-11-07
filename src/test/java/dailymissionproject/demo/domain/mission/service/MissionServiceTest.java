package dailymissionproject.demo.domain.mission.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.image.service.ImageService;
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
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("[unit] [service] MissionService")
@ExtendWith(MockitoExtension.class)
class MissionServiceTest {
    @InjectMocks
    private MissionService missionService;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
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
                when(missionRepository.findAllByParticipantSize(pageable)).thenReturn(MissionObjectFixture.getHotMissionListPageable());

                PageResponseDto pageResponseDto = missionService.findHotList(pageable);
                List<MissionHotListResponseDto> list = (List<MissionHotListResponseDto>) pageResponseDto.content();

                assertEquals(list.get(0).getTitle(),
                        MissionObjectFixture.getHotMissionListPageable().getContent().get(0).getTitle());
            }

            @Test
            @DisplayName("신규 미션 리스트를 조회할 수 있다.")
            void test_mission_read_new_list_success() {
                when(missionRepository.findAllByCreatedInMonth(pageable)).thenReturn(MissionObjectFixture.getNewMissionListPageable());

                PageResponseDto pageResponseDto = missionService.findNewList(pageable);
                List<MissionNewListResponseDto> list = (List<MissionNewListResponseDto>) pageResponseDto.content();

                assertEquals(list.get(0).getTitle(),
                        MissionObjectFixture.getNewMissionListPageable().getContent().get(0).getTitle());
            }

            @Test
            @DisplayName("전체 미션 리스트를 조회할 수 있다.")
            void test_mission_read_all_list_success() {
                when(missionRepository.findAllByCreatedDate(pageable)).thenReturn(MissionObjectFixture.getAllMissionListPageable());

                PageResponseDto pageResponseDto = missionService.findAllList(pageable);
                List<MissionAllListResponseDto> list = (List<MissionAllListResponseDto>) pageResponseDto.content();

                assertEquals(list.get(0).getTitle(),
                        MissionObjectFixture.getAllMissionListPageable().getContent().get(0).getTitle());
            }

            /*
            @Test
            @DisplayName("유저가 참여중인 미션 리스트를 조회할 수 있다.")
            void test_mission_read_user_joining_list_success(){
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(missionRepository.findAll()).thenReturn(MissionObjectFixture.getMissionList());

                List<MissionUserListResponseDto> userMissionResponse = missionService.findByUserList(oAuth2User);

                assertFalse(userMissionResponse.isEmpty());
                assertEquals(userMissionResponse.get(0).getTitle(), MissionObjectFixture.getMissionList().get(0).getTitle());
            }


             */

        }
    }
