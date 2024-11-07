package dailymissionproject.demo.domain.post.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.meta.MetaService;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;
@RestController
@Tag(name = "포스트(인증글)", description = "포스트 관련 API 입니다.")
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    /**
     * 인증글을 작성하는 API
     * @param user
     * @param postSaveReqDto
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/save")
    @Operation(summary = "포스트 생성", description = "사용자가 포스트를 생성할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> save(@CurrentUser CustomOAuth2User user
                    , @Valid @RequestPart  PostSaveRequestDto postSaveReqDto)throws IOException {

        return ResponseEntity.ok(success(postService.save(user, postSaveReqDto)));
    }

    /**
     * 인증글에 대한 상세 조회를 요청할 때 사용하는 API
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    @Operation(summary = "포스트 상세 조회", description = "포스트 상세 조회할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 조회에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(success(postService.findById(id)));
    }

    /**
     * 유저별로 제출한 전체 인증글 조회를 요청할 때 사용하는 API
     * @param user
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    @Operation(summary = "유저별 전체 포스트 조회", description = "유저가 제출한 포스트 목록을 조회할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 조회에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findByUser(@CurrentUser CustomOAuth2User user, Pageable pageable){
        PageResponseDto response = postService.findAllByUser(user, pageable);

        return ResponseEntity.ok(success(response.content(), MetaService.createMetaInfo().add("isNext", response.next())));
    }

    /**
     * 미션별로 작성한 인증글 조회 요청할 때 사용하는 API
     * @param id
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/mission/{id}")
    @Operation(summary = "미션별 전체 포스트 조회", description = "미션별 전체 포스트 목록을 조회할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 조회에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findByMission(@PathVariable("id") Long id, Pageable pageable){
        PageResponseDto response = postService.findAllByMission(id, pageable);

        return ResponseEntity.ok(success(response.content(), MetaService.createMetaInfo().add("isNext", response.next())));
    }

    /**
     * 포스트를 수정할 때 사용하는 API
     * @param id
     * @param postUpdateRequestDto
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    @Operation(summary = "포스트 수정", description = "포스트를 수정할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 수정에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> updateById(@PathVariable("id") Long id
                        , @RequestPart PostUpdateRequestDto postUpdateRequestDto
                        , @CurrentUser CustomOAuth2User user) throws IOException {
        return ResponseEntity.ok(success(postService.update(id, postUpdateRequestDto, user)));
    }

    /**
     * 포스트를 삭제할 때 사용하는 API
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "포스트 삭제", description = "포스트를 삭제할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 삭제에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> deleteById(@PathVariable("id") Long id, @CurrentUser CustomOAuth2User user){
        return ResponseEntity.ok(success(postService.deleteById(id, user)));
        }
    }
