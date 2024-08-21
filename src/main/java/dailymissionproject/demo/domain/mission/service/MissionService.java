package dailymissionproject.demo.domain.mission.service;


import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.INVALID_DELETE_REQUEST;
import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.MISSION_NOT_FOUND;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;


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
    @Cacheable(value = "mission", key = "'info-' + #id")
    public MissionResponseDto findById(Long id){

        Mission findMission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        MissionResponseDto missionResponseDto = MissionResponseDto.of(findMission);
        missionResponseDto.setParticipants(findMission.getAllParticipantUser());

        return missionResponseDto;
    }


    //== 미션 생성 ==//
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "missionLists", allEntries = true),
            @CacheEvict(value = "mission", allEntries = true)
    })
    public MissionSaveResponseDto save(String username, MissionSaveRequestDto missionReqDto, MultipartFile file) throws IOException {

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        //참여 코드 5글자로 설정
        String credential = String.valueOf(UUID.randomUUID()).substring(0, 5);
        String imgUrl = imageService.uploadImg(file);

        Mission mission = missionReqDto.toEntity(findUser);

        mission.setImgUrl(imgUrl);
        mission.setCredential(credential);

        //미션 시작일자 검증
        mission.isValidStartDate(LocalDate.now());

        Long id = missionRepository.save(mission).getId();

        //미션 생성 시 방장은 자동 참여
        Participant participant = Participant.builder()
                                             .mission(mission)
                                             .user(findUser)
                                             .build();

        participantRepository.save(participant);

        MissionSaveResponseDto responseDto = MissionSaveResponseDto.builder()
                                                                    .credential(credential)
                                                                    .build();
        return responseDto;
    }

    /*
    *미션 삭제
    * 방장만 가능하고, 시작되지 않았고, 참여자가 없어야함
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "missionLists", allEntries = true),
            @CacheEvict(value = "mission", allEntries = true)
    })
    public boolean delete(Long id, String username){

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        mission.isDeletable(findUser);

        if(mission.getParticipantCountNotBanned() > 1){
            throw new MissionException(INVALID_DELETE_REQUEST);
        }
        missionRepository.delete(mission);
        return true;
    }

    //Hot 미션 불러오기 ==//
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'hot-' + #pageable.getPageNumber()")
    public PageResponseDto findHotList(Pageable pageable){

        Slice<MissionHotListResponseDto> responseList = missionRepository.findAllByParticipantSize(pageable);

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    //New 미션 불러오기 ==//
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'new-' + #pageable.getPageNumber()")
    public PageResponseDto findNewList(Pageable pageable){

        Slice<MissionNewListResponseDto> responseList = missionRepository.findAllByCreatedInMonth(pageable);

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    //모든 미션 불러오기 ==//
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'all-' + #pageable.getPageNumber()")
    public PageResponseDto findAllList(Pageable pageable){

        Slice<MissionAllListResponseDto> responseList = missionRepository.findAllByCreatedDate(pageable);

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    @Transactional(readOnly = true)
    public List<MissionAllListResponseDto> findAllList(){

        List<MissionAllListResponseDto> res = new ArrayList<>();

        List<Mission> allLists = missionRepository.findAllByCreatedDate();

        for(Mission mission : allLists){
            MissionAllListResponseDto allMission =
                    MissionAllListResponseDto.builder()
                                            .id(mission.getId())
                                            .title(mission.getTitle())
                                            .content(mission.getContent())
                                            .imageUrl(mission.getImageUrl())
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
                    .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

            if(mission.isEndAble(LocalDate.now())){
                mission.end();
            }
        }

    }
}
