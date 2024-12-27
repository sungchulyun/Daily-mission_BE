package dailymissionproject.demo.domain.notify.controller;


import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.meta.MetaService;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.notify.service.EmitterService;
import dailymissionproject.demo.domain.notify.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;

@RestController
@RequiredArgsConstructor
@Tag(name = "알림", description = "알림 관련 API 입니다.")
@RequestMapping("/api/v1/notify")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 구독", description = "클라이언트가 실시간 이벤트를 구독할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public SseEmitter subscribe(@CurrentUser CustomOAuth2User user) {
        return notificationService.subscribe(user.getId());
    }

    @GetMapping(value = "/user")
    @Operation(summary = "알림 조회", description = "사용자별 알림 목록을 확인할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> getUserNotifications(@CurrentUser CustomOAuth2User user, Pageable pageable) {
        PageResponseDto response = notificationService.getUserNotifications(user, pageable);

        return ResponseEntity.ok(success(response.content(), MetaService.createMetaInfo().add("isNext", response.next())));
    }

    @PutMapping("/{id}") @Operation(summary = "알림 읽음 표사", description = "알림 읽을 표시할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "포스트 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> checkNotification(@CurrentUser CustomOAuth2User user, @PathVariable Long id) {
        return ResponseEntity.ok(success(notificationService.readNotification(user, id)));
    }
}
