package dailymissionproject.demo.domain.participant.service;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;

    @Transactional
    public boolean save(String username, ParticipantSaveRequestDto requestDto){

        //미션 null값 검증
        if(requestDto.getMission() == null){
            throw new IllegalArgumentException("참여할 미션을 선택하지 않았습니다.");
        }

        //미션 id 유효한지 검증
        Mission mission = missionRepository.findById(requestDto.getMission().getId())
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 존재하지 않습니다."));

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        //해당 사용자가 해당 미션에 참여한 이력이 있는지 검증
        Optional<Participant> optional = participantRepository.findByMissionAndUser(mission, findUser);
        if(optional.isPresent()){
            if(optional.get().isBanned()){
                throw new IllegalArgumentException("강퇴된 미션에는 다시 참여할 수 없습니다.");
            } else {
                throw new IllegalArgumentException("이미 참여중인 미션입니다.");
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
        if(!requestDto.getCredential().equals(mission.getCredential())){
            throw new RuntimeException("참여코드를 확인해주세요.");
        }

        if(!(mission.isPossibleToParticipate(LocalDate.now()))){
            throw new RuntimeException("해당 미션은 참여가 불가능한 미션입니다.");
        }

        Participant participant = requestDto.toEntity(findUser);
        participantRepository.save(participant);
        return true;
    }
}
