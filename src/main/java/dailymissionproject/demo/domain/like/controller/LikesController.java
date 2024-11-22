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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 포스트가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> save(@PathVariable Long postId, @CurrentUser CustomOAuth2User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        LikesRequestDto likesRequest = LikesRequestDto.builder()
                .postId(postId)
                .userId(findUser.getId())
                .build();

        return ResponseEntity.ok(success(likesService.save(likesRequest)));
    }
}
