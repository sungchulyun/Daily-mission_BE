package dailymissionproject.demo.domain.post.service;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.dto.DateDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.post.dto.PostScheduleResponseDto;
import dailymissionproject.demo.domain.post.dto.PostSubmitDto;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(String username, PostSaveRequestDto requestDto){

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(requestDto.getMissionId())
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 존재하지 않습니다."));


        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        //미션 참여자인지 검증
        validIsParticipating(findUser, mission);

        Post post = requestDto.toEntity(findUser, mission);
        return postRepository.save(post).getId();
    }

    //== 포스트 상세조회==//
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id){

        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 포스트가 존재하지 않습니다. id : " + id));

        PostResponseDto responseDto = PostResponseDto.builder()
                                    .post(post)
                                    .build();
        return responseDto;
    }

    //== 사용자가 작성한 전체 포스트 조회==//
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByUser(String username){

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        List<Post> lists = postRepository.findAllByUser(findUser);

        List<PostResponseDto> responseList = new ArrayList<>();
        for(Post post : lists){
            responseList.add(new PostResponseDto(post));
        }
        return responseList;
    }

    //== 미션별 작성된 전체 포스트 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByMission(Long id){

        Mission mission = missionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 미션이 존재하지 않습니다. id" + id));

        List<Post> lists = postRepository.findAllByMission(mission);

        List<PostResponseDto> responseList = new ArrayList<>();
        for(Post post : lists){
            responseList.add(new PostResponseDto(post));
        }
        return responseList;
    }

    //==포스트 제출 이력 조회==//
    @Transactional(readOnly = true)
    public PostScheduleResponseDto findSchedule(Long id, Long week){

        /*
        * 설명 : 현재 요일에서 가장 가까운 일요일을 찾는다(한 주의 시작)
        * ex ) 월 -> 어제인 일요일
         */
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).minusWeeks(week);

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("해당 미션은 존재하지 않거나 폐기되었습니다. id"+ id));

        /*
        * Week주의 일주일간 날짜 & 제출의무 요일 확인
        * ex) 메서드 호출 일자 : 2024-07-31 / week : 0
        *       -> 2024-07-28  ~ 2024-08-02
        *       -> false/true/true/true/true/true/false
         */
        List<DateDto> weekDates = mission.getWeekDates(startDate);
        /*
        * 미션별 weekly 제출 이력을 postSubmitDto 객체로 전달받는다.
        * 새벽 3시 이전 제출 -> 전날 제출한 것으로 변환
         */
        List<PostSubmitDto> submits = postRepository.findWeeklyPostSubmitByMission(id, startDate);

        return PostScheduleResponseDto.builder()
                .weekDates(weekDates)
                .histories(null)
                .build();
    }

    //== 포스트 수정==//
    @Transactional
    public Long updateById(Long id, PostUpdateRequestDto requestDto){

        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 포스트입니다."+ id));

        post.update(requestDto.getTitle(), requestDto.getContent());
        return postRepository.save(post).getId();
    }

    //== 포스트 삭제==//
    @Transactional
    public boolean deleteById(Long id){

        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 포스트입니다. id =" + id));
        postRepository.deleteById(id);
        return true;
    }

    private boolean validIsParticipating(User user, Mission mission){

        for(Participant p : mission.getParticipants()){
            if(p.getId() == user.getId())
                return true;
        }
        throw new RuntimeException("참여중이지 않은 미션에 인증 글을 작성할 수 없습니다.");
    }

    @Transactional(readOnly = true)
    public boolean isSubmitToday(Participant participant, LocalDateTime now){

        boolean isSubmit = false;

        /*
        * 설명 : 현재시간 0시 ~ 3시 : 전날 03시01분 ~ 현재시간까지 제출 여부 확인
        *        현재시간 3시 ~ 24시 : 금일 03시 ~ 현재시간까지 제출 여부 확인
        *
        * 설명 : 매일 03시에 강퇴 처리를 수행하기 때문에,
        * -> 전날 새벽3시~ 다음날 새벽3시를 각각 분기 처리
         */

        LocalDateTime criteria = LocalDate.now().atTime(03,00);
        if(now.isBefore(criteria)){
            //전날 새벽 3시 ~ 현재
            isSubmit = postRepository.countPostSubmit(participant.getMission()
            , participant.getUser()
            ,criteria.minusDays(1)
            , now) > 0;

        } else {
            //금일 새벽 3시  ~ 현재
            isSubmit = postRepository.countPostSubmit(participant.getMission()
            ,participant.getUser()
            ,criteria
            ,now) > 0;
        }
        return isSubmit;
    }
}
