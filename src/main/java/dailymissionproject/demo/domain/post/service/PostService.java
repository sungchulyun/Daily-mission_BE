package dailymissionproject.demo.domain.post.service;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //== 포스트 저장==//
    @Transactional
    public Long save(String userName, PostSaveRequestDto requestDto){
        log.info("미션 ID : {}", requestDto.getMissionId());
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(requestDto.getMissionId())
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 존재하지 않습니다. MissionId : " + requestDto.getMissionId()));

        User findUser = userRepository.findOneByName(userName);
        if(Objects.isNull(findUser)){
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. Name : " + userName);
        }

        Post post = requestDto.toEntity(findUser, mission);
        return postRepository.save(post).getId();
    }

    //== 포스트 상세조회==//
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id){

        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 포스트가 존재하지 않습니다. id : " + id));

        PostResponseDto responseDto = new PostResponseDto(post);
        return responseDto;
    }

    //== 사용자가 작성한 전체 포스트 조회==//
    @Transactional(readOnly = true)
    public List<PostResponseDto> findByUser(Long id){

        User user = userRepository.findOne(id);
        if (Objects.isNull(user)) {
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. UserId : " + id);
        }

        List<Post> lists = postRepository.findAllByUser(user);

        List<PostResponseDto> responseList = new ArrayList<>();
        for(Post post : lists){
            responseList.add(new PostResponseDto(post));
        }
        return responseList;
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
    }
}
