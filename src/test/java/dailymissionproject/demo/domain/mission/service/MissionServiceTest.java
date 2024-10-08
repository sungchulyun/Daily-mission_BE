package dailymissionproject.demo.domain.mission.service;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@Tag("unit")
@DisplayName("[unit] [service] MissionService")
@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private MissionService missionService;

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



}
