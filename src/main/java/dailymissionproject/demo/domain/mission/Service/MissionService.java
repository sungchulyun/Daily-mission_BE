package dailymissionproject.demo.domain.mission.Service;

import dailymissionproject.demo.domain.mission.dto.request.MissionReqDto;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    public void createMission(MissionReqDto missionReqDto){
        Mission mission = Mission.builder()
                .title(missionReqDto.getTitle())
                .content(missionReqDto.getContent())
                .imageUrl(missionReqDto.getImgUrl())
                .build();
        missionRepository.save(mission);
    }

}
