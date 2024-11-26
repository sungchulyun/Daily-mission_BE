package dailymissionproject.demo.domain.participant.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.notify.service.EmitterService;
import dailymissionproject.demo.domain.notify.service.NotificationService;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.exception.ParticipantException;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.post.service.PostService;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.MISSION_NOT_FOUND;
import static dailymissionproject.demo.domain.participant.exception.ParticipantExceptionCode.*;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final PostService postService;
    private final NotificationService notificationService;

    /**
     *
     * @param user
     * @param requestDto
     * 미션 참여 기능
     * @return boolean
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "mission", allEntries = true)
    })
    public boolean save(CustomOAuth2User user, ParticipantSaveRequestDto requestDto){

        Mission findMission = missionRepository.findById(requestDto.getMissionId()).orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        //해당 사용자가 해당 미션에 참여한 이력이 있는지 검증
        Optional<Participant> optional = participantRepository.findByMissionAndUser(findMission, findUser);

        if(optional.isPresent()){
            if(optional.get().isBanned()){
                throw new ParticipantException(BAN_HISTORY_EXISTS);
            } else {
                throw new ParticipantException(PARTICIPANT_ALREADY_EXISTS);
            }
        }

        /**
         * 설명 : 참여 가능한 미션인지 검증 추가 필요!
         * 1. 참여코드가 맞는지
         * 2. 종료되지 않은 미션
         * 3. 삭제되지 않은 미션
         * 4. 시작되지 않은 미션
         */

        //참여 코드 검증
        if(!requestDto.getCredential().equals(findMission.getCredential())){
            throw new ParticipantException(INVALID_INPUT_VALUE_CREDENTIAL);
        }

        if(!(findMission.isPossibleToParticipate(LocalDate.now()))){
            throw new ParticipantException(INVALID_PARTICIPATE_REQUEST);
        }

        Participant participant = requestDto.toEntity(findUser, findMission);
        participantRepository.save(participant);

        sendParticipationNotify(findUser, findMission);
        return true;
    }

    /**
     * 신규 미션 참여자가 발생할 경우 참여중인 유저들에게 푸시 알람을 보낸다.
     * 미션에 참여한 당사자를 제외하고 알림 발생
     * @param newUser
     * @param mission
     */
    private void sendParticipationNotify(User newUser, Mission mission){

        for(Participant participant : mission.getParticipants()){
            if((participant.getUser().getId().equals(newUser.getId()))){
                continue;
            }

            notificationService.createNotification(participant.getUser(), NotificationType.PARTICIPATE, "신규 참여자 " + newUser.getNickname() + "님이 참여했습니다.");
        }
    }

    @Transactional
    public void ban(){

        LocalDateTime now = LocalDateTime.now();

        List<Mission> missionList = missionRepository.findAll();

        for(Mission mission : missionList){
            for(Participant p : mission.getParticipants()){
                boolean submitted = postService.isSubmitToday(p, now);

                if(!submitted){
                    p.ban();
                }
            }
        }
    }
}
