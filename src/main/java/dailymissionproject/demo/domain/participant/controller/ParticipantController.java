package dailymissionproject.demo.domain.participant.controller;

import dailymissionproject.demo.domain.participant.dto.request.ParticipantSaveRequestDto;
import dailymissionproject.demo.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    //== 미션 참여==//
    @PostMapping("/join/{userName}")
    public boolean save(@PathVariable("userName")String userName, @RequestBody ParticipantSaveRequestDto requestDto){
        return participantService.save(userName, requestDto);
    }
}
