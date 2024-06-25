package dailymissionproject.demo.domain.mission.Service;

import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    //==미션 상세 조회==//
    @Transactional(readOnly = true)
    public MissionResponseDto findById(Long id){

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 존재하지 않습니다."));

        MissionResponseDto responseDto = new MissionResponseDto(mission);
        return responseDto;
    }


    //== 미션 생성 ==//
    @Transactional
    public MissionSaveResponseDto save(String userName, MissionSaveRequestDto missionReqDto, MultipartFile file) throws IOException {

        User findUser = userRepository.findOneByName(userName);
        if(Objects.isNull(findUser)){
            throw new RuntimeException("존재하지 않는 사용자 닉네임입니다.");
        }



        String credential = String.valueOf(UUID.randomUUID());

        String imgUrl = imageService.uploadImg(file);

        Mission mission = Mission.builder()
                        .user(findUser)
                        .title(missionReqDto.getTitle())
                        .content(missionReqDto.getContent())
                        .imageUrl(imgUrl)
                        .credential(credential)
                        .startDate(missionReqDto.getStartDate())
                        .endDate(missionReqDto.getEndDate())
                        .build();

        mission.isValidStartDate(LocalDate.now());
        missionRepository.save(mission);

        MissionSaveResponseDto responseDto = MissionSaveResponseDto.builder()
                .credential(credential).build();
        return responseDto;

        //미션 생성자를 바로 참여하는 로직 추가 필요
    }

    /*
    *미션 삭제
    * 방장만 가능하고, 시작되지 않았고, 참여자가 없어야함
     */
    @Transactional
    public boolean delete(Long id, String userName){

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 미션입니다."));

        User findUser = userRepository.findOneByName(userName);

        mission.isDeletable(findUser);
        if(mission.getParticipantCountNotBanned() > 1){
            throw new RuntimeException("다른 참여자가 참여중인 미션은 삭제가 불가능합니다.");
        }
        missionRepository.delete(mission);
        return true;
    }

    //Hot 미션 불러오기 ==//
    @Transactional(readOnly = true)
    public List<MissionHotListResponseDto> findHotList(){

        List<MissionHotListResponseDto> res = new ArrayList<>();

        List<Mission> hotLists = missionRepository.findAllByParticipantSize();
        for(Mission mission : hotLists){
            MissionHotListResponseDto hotMission = MissionHotListResponseDto.builder()
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .userName(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(hotMission);
        }
        return res;
    }

    //New 미션 불러오기 ==//
    @Transactional(readOnly = true)
    public List<MissionNewListResponseDto> findNewList(){

        List<MissionNewListResponseDto> res = new ArrayList<>();

        List<Mission> newLists = missionRepository.findAllByCreatedInMonth();
        for(Mission mission : newLists){
            MissionNewListResponseDto newMission = MissionNewListResponseDto.builder()
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .userName(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(newMission);
        }
        return res;
    }

    //모든 미션 불러오기 ==//
    @Transactional(readOnly = true)
    public List<MissionAllListResponseDto> findAllList(){

        List<MissionAllListResponseDto> res = new ArrayList<>();

        List<Mission> allLists = missionRepository.findAllByCreatedDate();
        for(Mission mission : allLists){
            MissionAllListResponseDto allMission = MissionAllListResponseDto.builder()
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .userName(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(allMission);
        }
        return res;
    }
}
