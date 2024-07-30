package dailymissionproject.demo.domain.post.service;


import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.MISSION_NOT_FOUND;
import static dailymissionproject.demo.domain.post.exception.PostExceptionCode.*;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.dto.DateDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.post.dto.PostScheduleResponseDto;
import dailymissionproject.demo.domain.post.dto.PostSubmitDto;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.exception.PostException;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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
    private final ImageService imageService;

    @Transactional
    @Caching(evict = {
            //전체 포스트
            @CacheEvict(value = "postLists", key = "'all'"),
            @CacheEvict(value = "postLists", key = "'user-' + #user.getUsername()" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    public Long save(String username, PostSaveRequestDto requestDto, MultipartFile file) throws IOException {

        Mission mission = missionRepository.findByIdAndDeletedIsFalse(requestDto.getMissionId())
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        //미션 참여자인지 검증
        validIsParticipating(findUser, mission);

        String imgUrl = imageService.uploadImg(file);

        Post post = requestDto.toEntity(findUser, mission);
        post.setImageUrl(imgUrl);

        return postRepository.save(post).getId();
    }

    //== 포스트 상세조회==//
    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#id")
    public PostResponseDto findById(Long id){

        Post post = postRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        PostResponseDto responseDto = PostResponseDto.builder()
                                    .post(post)
                                    .build();
        return responseDto;
    }

    //== 사용자가 작성한 전체 포스트 조회==//
    @Transactional(readOnly = true)
    @Cacheable(value = "postLists", key = "'user-' + #username")
    public PageResponseDto findAllByUser(String username, Pageable pageable){

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Slice<PostResponseDto> userPostLists = postRepository.findAllByUser(pageable, findUser);
        if(userPostLists.getContent().size() == 0){
            throw new PostException(EMPTY_POST_HISTORY);
        }
        PageResponseDto pageResponseDto = new PageResponseDto(userPostLists.getContent(), userPostLists.hasNext());

        /*List<PostResponseDto> responseList = new ArrayList<>();
        for(Post post : userPostLists){
            responseList.add(new PostResponseDto(post));
        }
        return responseList;
         */
        return pageResponseDto;
    }
    /*
    public List<PostResponseDto> findAllByUser(String username){

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        List<Post> lists = postRepository.findAllByUser(findUser);

        List<PostResponseDto> responseList = new ArrayList<>();
        for(Post post : lists){
            responseList.add(new PostResponseDto(post));
        }
        return responseList;
    }
     */

    //== 미션별 작성된 전체 포스트 조회
    @Transactional(readOnly = true)
    @Cacheable(value = "postLists", key = "'mission-' + #id")
    public PageResponseDto findAllByMission(Long id, Pageable pageable){

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

        Slice<PostResponseDto> missionPostList = postRepository.findAllByMission(pageable, mission);

        PageResponseDto pageResponseDto = new PageResponseDto<>(missionPostList.getContent(), missionPostList.hasNext());

        return pageResponseDto;
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
                .orElseThrow(() -> new MissionException(MISSION_NOT_FOUND));

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
    @Caching(evict = {
            //전체 포스트
            @CacheEvict(value = "postLists", key = "'all'"),
            @CacheEvict(value = "postLists", key = "'user-' + #user.getUsername()" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    public Long updateById(Long id, MultipartFile file, PostUpdateRequestDto requestDto) throws IOException {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        String imgUrl = imageService.uploadImg(file);

        post.update(requestDto.getTitle(), requestDto.getContent(), imgUrl);

        return postRepository.save(post).getId();
    }

    //== 포스트 삭제==//
    @Transactional
    @Caching(evict = {
            //전체 포스트
            @CacheEvict(value = "postLists", key = "'all'"),
            @CacheEvict(value = "postLists", key = "'user-' + #user.getUsername()" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    public boolean deleteById(Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        postRepository.deleteById(id);
        return true;
    }

    private boolean validIsParticipating(User user, Mission mission){

        for(Participant p : mission.getParticipants()){
            if(p.getUser().getId() == user.getId())
                return true;
        }
        throw new PostException(INVALID_POST_SAVE_REQUEST);
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
