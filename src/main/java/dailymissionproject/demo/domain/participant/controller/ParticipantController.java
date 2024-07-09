package dailymissionproject.demo.domain.participant.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    //== 미션 참여==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/join")
    public boolean save(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody ParticipantSaveRequestDto requestDto){
        return participantService.save(user.getUsername(), requestDto);
    }
}
