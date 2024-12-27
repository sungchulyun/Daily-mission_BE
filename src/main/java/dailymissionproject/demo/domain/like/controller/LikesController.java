package dailymissionproject.demo.domain.like.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.like.dto.request.LikesRequestDto;
import dailymissionproject.demo.domain.like.service.LikesService;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;

@RestController
@RequiredArgsConstructor
@Tag(name = "좋아요", description = "좋아요 관련 API입니다.")
@RequestMapping("/api/v1/like")
public class LikesController {

    private final LikesService likesService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{postId}")
    @Operation(summary = "좋아요 추가", description = "사용자가 포스트에 대한 좋아요를 추가하고 싶을 때 사용하는 API입니다.")
    public ResponseEntity<GlobalResponse> add(@PathVariable Long postId, @CurrentUser CustomOAuth2User user) {
        return ResponseEntity.ok(success(likesService.addLike(postId, user.getId())));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{postId}")
    @Operation(summary = "좋아요 취소", description = "사용자가 포스트에 대한 좋아요를 취소하고 싶을 때 사용하는 API입니다.")
    public ResponseEntity<GlobalResponse> delete(@PathVariable Long postId, @CurrentUser CustomOAuth2User user) {
        return ResponseEntity.ok(success(likesService.removeLike(postId, user.getId())));
    }
}
