package dailymissionproject.demo.domain.mission.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionDetailResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionSaveResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionUpdateResponseDto;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp(){
        userRepository.save(user);

        mission.setUser(user);
        missionRepository.save(mission);

    }

    @Nested
    @DisplayName("미션 조회 서비스 레이어 테스트")
    class MissionReadServiceTest {

        @Test
        @DisplayName("미션을 상세조회 할 수 있다.")
        void test_mission_detail_read(){
            when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
            when(missionService.findById(anyLong())).thenReturn(missionDetailResponse);

            //MissionDetailResponseDto missionDetailResponse = missionService.findById(anyLong());

            //then
            assertEquals(mission.getTitle(), missionDetailResponse.getTitle());
        }
    }

}
