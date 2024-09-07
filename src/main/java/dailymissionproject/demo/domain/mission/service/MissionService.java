package dailymissionproject.demo.domain.mission.service;


import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
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

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.*;
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
    public MissionDetailResponseDto findById(Long id){

        Mission findMission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

       return MissionDetailResponseDto.builder()
                .title(findMission.getTitle())
                .content(findMission.getContent())
                .imageUrl(findMission.getImageUrl())
                .nickname(findMission.getUser().getNickname())
                .hint(findMission.getHint())
                .participantUserDto(findMission.getAllParticipantUser())
                .startDate(findMission.getStartDate())
                .endDate(findMission.getEndDate())
                .missionRuleResponseDto(MissionRuleResponseDto.of(findMission.getMissionRule()))
                .build();
    }


    //== 미션 생성 ==//
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "missionLists", allEntries = true),
            @CacheEvict(value = "mission", allEntries = true)
    })
    public MissionSaveResponseDto save(CustomOAuth2User user, MissionSaveRequestDto missionReqDto, MultipartFile file) throws IOException {

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        String imageUrl = imageService.uploadMissionS3(file, missionReqDto.getTitle());

        Mission mission = missionReqDto.toEntity(findUser);

        mission.setImageUrl(imageUrl);

        mission.isValidStartDate(LocalDate.now());

        Participant participant = Participant.builder()
                                             .mission(mission)
                                             .user(findUser)
                                             .build();

        participantRepository.save(participant);

        MissionSaveResponseDto responseDto = MissionSaveResponseDto.builder()
                                                                    .credential(mission.getCredential())
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
    public boolean delete(Long id, CustomOAuth2User user){

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        User findUser = userRepository.findById(user.getId())
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
    @Cacheable(value = "missionLists", key = "'hot-' + 'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
    public PageResponseDto findHotList(Pageable pageable){

        Slice<MissionHotListResponseDto> responseList = missionRepository.findAllByParticipantSize(pageable);

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    //New 미션 불러오기 ==//
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'new-' + 'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
    public PageResponseDto findNewList(Pageable pageable){

        Slice<MissionNewListResponseDto> responseList = missionRepository.findAllByCreatedInMonth(pageable);

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    //모든 미션 불러오기 ==//
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'all-' + 'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
    public PageResponseDto findAllList(Pageable pageable){

        Slice<MissionAllListResponseDto> responseList = missionRepository.findAllByCreatedDate(pageable);

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    //유저가 참여중인 미션 불러오기==//
    @Transactional(readOnly = true)
    public List<MissionUserListResponseDto> findByUserList(CustomOAuth2User user){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        List<MissionUserListResponseDto> responses = new ArrayList<>();;

        List<Mission> MissionList = missionRepository.findAll();

        for(Mission mission : MissionList){
            List<Participant> participantList = mission.getParticipants();

            for(Participant p : participantList) {

                if (p.getUser().equals(findUser)) {

                    MissionUserListResponseDto dto = MissionUserListResponseDto.builder()
                            .id(mission.getId())
                            .title(mission.getTitle())
                            .content(mission.getContent())
                            .imageUrl(mission.getImageUrl())
                            .nickname(mission.getUser().getNickname())
                            .startDate(mission.getStartDate())
                            .endDate(mission.getEndDate())
                            .ended(mission.isEnded())
                            .build();

                    responses.add(dto);
                }
            }


        }
        if(!isParticipating(responses)) throw new MissionException(PARTICIPATING_MISSION_NOT_FOUND);

        return responses;
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
                                            .nickname(mission.getUser().getNickname())
                                            .startDate(mission.getStartDate())
                                            .endDate(mission.getEndDate())
                                            .build();

            res.add(allMission);
        }
        return res;
    }

    private boolean isParticipating(List<MissionUserListResponseDto> list){
        if(list.size() < 1) return false;
        return true;
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
