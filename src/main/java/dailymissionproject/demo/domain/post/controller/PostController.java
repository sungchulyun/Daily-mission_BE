package dailymissionproject.demo.domain.post.controller;

import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //== 인증 글 생성==//
    @PostMapping("/create/{name}")
    public Long save(@PathVariable("name")String userName, @RequestBody PostSaveRequestDto requestDto){
        return postService.save(userName, requestDto);
    }

    //== 인증 글 상세 조회==//
    @GetMapping("/getInfo/{id}")
    public PostResponseDto findById(@PathVariable("id") Long id){
        return postService.findById(id);
    }

    //== 유저별 전체 포스트 목록 불러오기==//
    @GetMapping("/getUser/{id}")
    public List<PostResponseDto> findByUser(@PathVariable("id") Long id){
        return postService.findByUser(id);
    }

    //== 포스트 업데이트==//
    @PutMapping("/update{id}")
    public Long updateById(@PathVariable("id") Long id, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return postService.updateById(id, postUpdateRequestDto);
    }


    //== 포스트 삭제==//
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Long id){
        return;
    }

}
