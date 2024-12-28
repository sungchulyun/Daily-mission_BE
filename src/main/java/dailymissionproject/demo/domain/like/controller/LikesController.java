package dailymissionproject.demo.domain.like.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.like.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok((success(likesService.likePost(postId, user.getId()))));
    }
}
