package dailymissionproject.demo.domain.post.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //== 인증 글 생성==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        return postService.save(user.getUsername(), requestDto);
    }

    //== 인증 글 상세 조회==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getInfo/{id}")
    public PostResponseDto findById(@PathVariable("id") Long id){
        return postService.findById(id);
    }

    //== 유저별 전체 포스트 목록 불러오기==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getUser/{id}")
    public List<PostResponseDto> findByUser(@PathVariable("id") Long id){
        return postService.findByUser(id);
    }

    //== 포스트 업데이트==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update/{id}")
    public Long updateById(@PathVariable("id") Long id, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return postService.updateById(id, postUpdateRequestDto);
    }


    //== 포스트 삭제==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{id}")
    public boolean deleteById(@PathVariable("id") Long id){
        return postService.deleteById(id);
        }
    }
