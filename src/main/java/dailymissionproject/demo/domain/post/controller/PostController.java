package dailymissionproject.demo.domain.post.controller;

import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //== 인증 글 생성==//
    @PostMapping("/create/{id}/{name}")
    public Long save(@PathVariable("id")Long id, @PathVariable("name")String userName, @RequestBody PostSaveRequestDto requestDto){
        return postService.save(id, userName, requestDto);
    }

    //== 인증 글 상세 조회==//

    //== 사용자가 작성한 모든 인증 조회==//

    //== 인증 삭제==//

    //== 유저별 전체 포스트 목록 불러오기==//
}
