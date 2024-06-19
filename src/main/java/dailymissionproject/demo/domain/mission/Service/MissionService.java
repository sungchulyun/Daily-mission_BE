package dailymissionproject.demo.domain.mission.Service;

import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionSaveResponseDto;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    public MissionSaveResponseDto save(String userName, MissionSaveRequestDto missionReqDto){

        User findUser = userRepository.findOneByName(userName);
        if(Objects.isNull(findUser)){
            throw new RuntimeException("존재하지 않는 사용자 닉네임입니다.");
        }

        Mission mission = Mission.builder()
                        .title(missionReqDto.getTitle())
                        .content(missionReqDto.getContent())
                        .imageUrl(missionReqDto.getImgUrl())
                        .build();
        mission.setUser(findUser);
        missionRepository.save(mission);

        MissionSaveResponseDto responseDto = MissionSaveResponseDto.builder()
                .title(mission.getTitle())
                .content(mission.getContent())
                .imgUrl(mission.getImageUrl())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .build();
        return responseDto;
    }

}
