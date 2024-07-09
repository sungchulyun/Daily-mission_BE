package dailymissionproject.demo.domain.post.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    //== 인증 글 생성==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    @Caching(evict = {
            //전체 포스트
            @CacheEvict(value = "postLists", key = "'all'"),
            @CacheEvict(value = "postLists", key = "'user-' + #username" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    public Long save(@RequestBody PostSaveRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String username = user.getUsername();
        return postService.save(user.getUsername(), requestDto);
    }

    //== 인증 글 상세 조회==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getInfo/{id}")
    @Cacheable(value = "posts", key = "#id")
    public PostResponseDto findById(@PathVariable("id") Long id){
        return postService.findById(id);
    }

    //== 유저별 전체 포스트 목록 불러오기==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getUser")
    @Cacheable(value = "postLists", key = "'user-' + #user.username")
    public List<PostResponseDto> findByUser(@AuthenticationPrincipal CustomOAuth2User user){
        String username = user.getUsername();
        return postService.findAllByUser(username);
    }

    //== 미션별 전체 포스트 목록 불러오기==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("getMission/{id}")
    public List<PostResponseDto> findByMission(@PathVariable("id") Long id){
        return postService.findAllByMission(id);
    }

    //== 포스트 업데이트==//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public Long updateById(@PathVariable("id") Long id, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return postService.updateById(id, postUpdateRequestDto);
    }


    //== 포스트 삭제==//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public boolean deleteById(@PathVariable("id") Long id){
        return postService.deleteById(id);
        }
    }
