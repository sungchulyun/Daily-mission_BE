package dailymissionproject.demo.domain.post.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "포스트(인증글)", description = "포스트 관련 API 입니다.")
@RequestMapping("/api/post")
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
            @CacheEvict(value = "postLists", key = "'user-' + #user.getUsername()" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    @Operation(summary = "포스트 생성", description = "사용자가 포스트를 생성할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public Long save(@AuthenticationPrincipal CustomOAuth2User user
                    , @RequestPart PostSaveRequestDto postSaveReqDto
                    , @RequestPart MultipartFile file)throws IOException {

        return postService.save(user.getUsername(), postSaveReqDto, file);
    }

    //== 인증 글 상세 조회==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/detail/{id}")
    @Cacheable(value = "posts", key = "#id")
    @Operation(summary = "포스트 상세 조회", description = "포스트 상세 조회할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 조회에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public PostResponseDto findById(@PathVariable("id") Long id){
        return postService.findById(id);
    }

    //== 유저별 전체 포스트 목록 불러오기==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    @Cacheable(value = "postLists", key = "'user-' + #user.getUsername()")
    @Operation(summary = "유저별 전체 포스트 조회", description = "유저가 제출한 포스트 목록을 조회할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 조회에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public List<PostResponseDto> findByUser(@AuthenticationPrincipal CustomOAuth2User user){
        return postService.findAllByUser(user.getUsername());
    }

    //== 미션별 전체 포스트 목록 불러오기==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/mission/{id}")
    @Operation(summary = "미션별 전체 포스트 조회", description = "미션별 전체 포스트 목록을 조회할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 조회에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public List<PostResponseDto> findByMission(@PathVariable("id") Long id){
        return postService.findAllByMission(id);
    }

    //== 포스트 업데이트==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    @Caching(evict = {
            //전체 포스트
            @CacheEvict(value = "postLists", key = "'all'"),
            @CacheEvict(value = "postLists", key = "'user-' + #user.getUsername()" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    @Operation(summary = "포스트 수정", description = "포스트를 수정할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 수정에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public Long updateById(@PathVariable("id") Long id, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return postService.updateById(id, postUpdateRequestDto);
    }


    //== 포스트 삭제==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    @Caching(evict = {
            //전체 포스트
            @CacheEvict(value = "postLists", key = "'all'"),
            @CacheEvict(value = "postLists", key = "'user-' + #user.getUsername()" ),
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
    })
    @Operation(summary = "포스트 삭제", description = "포스트를 삭제할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 삭제에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public boolean deleteById(@PathVariable("id") Long id){
        return postService.deleteById(id);
        }
    }
