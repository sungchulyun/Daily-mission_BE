package dailymissionproject.demo.domain.participant.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participant")
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
    public boolean save(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody ParticipantSaveRequestDto requestDto){
        return participantService.save(user.getUsername(), requestDto);
    }
}
