package dailymissionproject.demo.domain.participant.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participant")
@Tag(name = "미션 참여", description = "미션 참여 관련 API입니다.")
public class ParticipantController {

    private final ParticipantService participantService;

    //== 미션 참여==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/join")
    @Operation(summary = "미션 참여", description = "미션에 참여할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "미션 참여에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> save(@CurrentUser CustomOAuth2User user, @RequestBody ParticipantSaveRequestDto requestDto){
        return ResponseEntity.ok(success(participantService.save(user, requestDto)));
    }
}
