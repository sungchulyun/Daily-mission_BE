package dailymissionproject.demo.domain.participant.controller;

import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    //== 미션 참여==//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/join/{userName}")
    public boolean save(@PathVariable("userName")String userName, @RequestBody ParticipantSaveRequestDto requestDto){
        return participantService.save(userName, requestDto);
    }
}
