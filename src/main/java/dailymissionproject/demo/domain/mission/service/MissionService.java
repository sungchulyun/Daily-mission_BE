package dailymissionproject.demo.domain.mission.service;


import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.image.service.ImageService;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    /**
     * 미션 상세 조회할 때 사용하는 메서드
     * @param id
     * @return MissionDetailResponseDto
     */
    @Transactional(readOnly = true)
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


    /**
     * 미션 생성할 때 사용하는 메서드
     * @param user : 로그인한 유저 객체
     * @param missionReqDto : 생성할 미션 정보가 담긴 DTO
     * @return MissionSaveResponseDto
     * @throws IOException
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "missionLists", allEntries = true),
    })
    public MissionSaveResponseDto save(CustomOAuth2User user, MissionSaveRequestDto missionReqDto) throws IOException {

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Mission mission = missionReqDto.toEntity(findUser);

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

    /**
     * 미션 수정할 때 사용하는 메서드
     * 설명 : 참여자가 방장을 제외하고 한명도 없을 때만, 방장의 권한으로 삭제가 가능하다.
     * @param id : 미션의 sequence ID
     * @param user : 현재 로그인한 유저
     * @return boolean
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "missionLists", allEntries = true),
    })
    public MissionUpdateResponseDto update(Long id, CustomOAuth2User user, MissionUpdateRequestDto request){

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Mission findMission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        log.info("로그인한 유저 정보 : {}", findUser.toString());
        log.info("수정할 미션의 방장 정보 : {}", findMission.getUser().toString());

        isUserHost(findMission, findUser);

        if(Objects.isNull(request.getCredential()) && Objects.isNull(request.getHint())){
            throw new MissionException(UPDATE_INPUT_IS_EMPTY);
        }

        Optional.ofNullable(request.getCredential()).ifPresent(findMission::setCredential);
        Optional.ofNullable(request.getHint()).ifPresent(findMission::setHint);

        return MissionUpdateResponseDto.builder()
                .hint(request.getHint())
                .credential(request.getCredential())
                .build();
    }

    /**
     * 미션 삭제할 때 사용하는 메서드
     * 설명 : 참여자가 방장을 제외하고 한명도 없을 때만, 방장의 권한으로 삭제가 가능하다.
     * @param id : 미션의 sequence ID
     * @param user : 현재 로그인한 유저
     * @return boolean
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "missionLists", allEntries = true),
    })
    public boolean delete(Long id, CustomOAuth2User user){
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Mission findMission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        log.info("로그인한 유저 정보 : {}", findUser.toString());
        log.info("수정할 미션의 방장 정보 : {}", findMission.getUser().toString());

        isUserHost(findMission, findUser);

        findMission.isDeletable(findUser);

        findMission.delete();

        return true;
    }

    /**
     * 인기 미션 리스트 조회할 때 사용하는 API
     * 설명 : 조회 결과를 Slice객체로 받아 다음 페이지가 있는지 여부와 함께 DTO에 담는다.
     * @param pageable : 쿼리스트링으로 Size, Page를 받는다.
     * @return PageResponseDto
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'hot-' + 'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
    @CircuitBreaker(name = "redis-circuit-breaker", fallbackMethod = "findHotListFallBack")
    public PageResponseDto findHotList(Pageable pageable, CustomOAuth2User user){
        Slice<MissionHotListResponseDto> responseList = missionRepository.findAllByParticipantSize(pageable, user.getId());

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    public PageResponseDto findHotListFallBack(Pageable pageable, CustomOAuth2User user, Throwable e){
        Slice<MissionHotListResponseDto> responseList = missionRepository.findAllByParticipantSize(pageable, user.getId());

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    /**
     * 신규 미션 리스트 조회할 때 사용하는 API
     * 설명 : 조회 결과를 Slice객체로 받아 다음 페이지가 있는지 여부와 함께 DTO에 담는다.
     * @param pageable : 쿼리스트링으로 Size, Page를 받는다.
     * @return PageResponseDto
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'new-' + 'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
    @CircuitBreaker(name = "redis-circuit-breaker", fallbackMethod = "findNewListFallBack")
    public PageResponseDto findNewList(Pageable pageable, CustomOAuth2User user){
        Slice<MissionNewListResponseDto> responseList = missionRepository.findAllByCreatedInMonth(pageable, user.getId());

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    public PageResponseDto findNewListFallBack(Pageable pageable, CustomOAuth2User user, Throwable e){
        Slice<MissionNewListResponseDto> responseList = missionRepository.findAllByCreatedInMonth(pageable, user.getId());

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    /**
     * 전체 미션 리스트 조회할 때 사용하는 API
     * 설명 : 조회 결과를 Slice객체로 받아 다음 페이지가 있는지 여부와 함께 DTO에 담는다.
     * @param pageable : 쿼리스트링으로 Size, Page를 받는다.
     * @return PageResponseDto
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "missionLists", key = "'all-' + 'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
    @CircuitBreaker(name = "redis-circuit-breaker", fallbackMethod = "findAllListFallBack")
    public PageResponseDto findAllList(Pageable pageable, CustomOAuth2User user){

        Slice<MissionAllListResponseDto> responseList = missionRepository.findAllByCreatedDate(pageable, user.getId());

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    public PageResponseDto findAllListFallBack(Pageable pageable, CustomOAuth2User user, Throwable e){
        Slice<MissionAllListResponseDto> responseList = missionRepository.findAllByCreatedDate(pageable, user.getId());

        PageResponseDto pageResponseDto = new PageResponseDto(responseList.getContent(), responseList.hasNext());

        return pageResponseDto;
    }

    /**
     * 현재 로그인한 유저가 참여 전적이 있는 미션 전체를 조회할 때 사용하는 API
     * 설명 : 전체 미션을 조회하고, 미션 참여자 리스트에 Request 유저가 있다면, List에 추가해 반환한다.
     * @param user : 쿼리스트링으로 Size, Page를 받는다.
     * @return PageResponseDto
     */
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

    /**
     * 전체 미션을 조회할 때 사용하는 메서드입니다.
     * 설명 : 배치 작업에서 미션을 종료할 때 사용합니다.
     * @return List<MissionAllListResponseDto>
     */
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

    /**
     * 유저가 참여중인 미션이 한개라도 있는지 여부를 검증하는 메서드
     * @param particiPatinglist
     * @return
     */
    public boolean isParticipating(List<MissionUserListResponseDto> particiPatinglist){
        if(particiPatinglist.size() < 1) return false;
        return true;
    }

    /**
     * 사용자가 수정 및 삭제 요청할 미션의 방장인지 여부를 검증하는 메서드
     * @param mission
     * @param user
     * @return
     */
    public boolean isUserHost(Mission mission, User user){
        if(!mission.getUser().equals(user)){
            throw new MissionException(INVALID_USER_REQUEST);
        }
        return true;
    }
}
