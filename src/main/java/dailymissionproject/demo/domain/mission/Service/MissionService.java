package dailymissionproject.demo.domain.mission.Service;

import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final ParticipantRepository participantRepository;
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
    public MissionSaveResponseDto save(String username, MissionSaveRequestDto missionReqDto, MultipartFile file) throws IOException {

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        //참여 코드 5글자로 설정
        String credential = String.valueOf(UUID.randomUUID()).substring(0, 5);
        String imgUrl = imageService.uploadImg(file);

        Mission mission = missionReqDto.toEntity(findUser);
        mission.setImageUrl(imgUrl);
        mission.setCredential(credential);

        mission.isValidStartDate(LocalDate.now());
        missionRepository.save(mission);

        //미션 생성 시 방장은 자동 참여
        Participant participant = Participant.builder()
                .mission(mission)
                .user(findUser)
                .build();
        participantRepository.save(participant);

        MissionSaveResponseDto responseDto = MissionSaveResponseDto.builder()
                .credential(credential).build();
        return responseDto;


    }

    /*
    *미션 삭제
    * 방장만 가능하고, 시작되지 않았고, 참여자가 없어야함
     */
    @Transactional
    public boolean delete(Long id, String username){

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 미션입니다."));

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        mission.isDeletable(findUser);
        if(mission.getParticipantCountNotBanned() > 1){
            throw new RuntimeException("다른 참여자가 참여중인 미션은 삭제가 불가능합니다.");
        }
        missionRepository.delete(mission);
        return true;
    }

    //Hot 미션 불러오기 ==//
    @Transactional(readOnly = true)
    public List<MissionHotListResponseDto> findHotList(Pageable pageable){

        List<MissionHotListResponseDto> res = new ArrayList<>();

        Slice<Mission> hotLists = missionRepository.findAllByParticipantSize(pageable);
        for(Mission mission : hotLists){
            MissionHotListResponseDto hotMission = MissionHotListResponseDto.builder()
                    .id(mission.getId())
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .imgUrl(mission.getImageUrl())
                    .name(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(hotMission);
        }
        return res;
    }

    //New 미션 불러오기 ==//
    @Transactional(readOnly = true)
    public List<MissionNewListResponseDto> findNewList(Pageable pageable){

        List<MissionNewListResponseDto> res = new ArrayList<>();

        Slice<Mission> newLists = missionRepository.findAllByCreatedInMonth(pageable);
        for(Mission mission : newLists){
            MissionNewListResponseDto newMission = MissionNewListResponseDto.builder()
                    .id(mission.getId())
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .imgUrl(mission.getImageUrl())
                    .name(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(newMission);
        }
        return res;
    }

    //모든 미션 불러오기 ==//
    @Transactional(readOnly = true)
    public List<MissionAllListResponseDto> findAllList(Pageable pageable){

        List<MissionAllListResponseDto> res = new ArrayList<>();

        Slice<Mission> allLists = missionRepository.findAllByCreatedDate(pageable);
        for(Mission mission : allLists){
            MissionAllListResponseDto allMission = MissionAllListResponseDto.builder()
                    .id(mission.getId())
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .imgUrl(mission.getImageUrl())
                    .name(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(allMission);
        }
        return res;
    }

    @Transactional(readOnly = true)
    public List<MissionAllListResponseDto> findAllList(){

        List<MissionAllListResponseDto> res = new ArrayList<>();

        List<Mission> allLists = missionRepository.findAllByCreatedDate();

        for(Mission mission : allLists){
            MissionAllListResponseDto allMission = MissionAllListResponseDto.builder()
                    .id(mission.getId())
                    .title(mission.getTitle())
                    .content(mission.getContent())
                    .imgUrl(mission.getImageUrl())
                    .name(mission.getUser().getName())
                    .startDate(mission.getStartDate())
                    .endDate(mission.getEndDate())
                    .build();

            res.add(allMission);
        }
        return res;
    }

    @Transactional
    public void end(List<MissionAllListResponseDto> missionLists){

        for(MissionAllListResponseDto missionDto : missionLists){
            Mission mission = missionRepository.findByIdAndDeletedIsFalse(missionDto.getId())
                    .orElseThrow(() -> new NoSuchElementException("해당 미션은 종료되었거나 폐기되었습니다."));

            if(mission.isEndAble(LocalDate.now())){
                mission.end();
            }
        }

    }
}
