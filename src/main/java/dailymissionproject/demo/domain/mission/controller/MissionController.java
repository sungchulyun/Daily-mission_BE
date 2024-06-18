package dailymissionproject.demo.domain.mission.controller;

import dailymissionproject.demo.domain.mission.Service.MissionService;
import dailymissionproject.demo.domain.mission.dto.request.MissionReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
public class MissionController {
    private final MissionService missionService;

    //== 미션 조회 ==//
    @GetMapping("/getInfo/{id}")
    public void getMission(){

    }

    //== 미션 생성==//
    @PostMapping("/create")
    public void createMission(MissionReqDto missionReqDto){
        missionService.createMission(missionReqDto);
    }

    //==미션 참여==//
    @PostMapping("/join/{id}")
    public void joinMission(@PathVariable("id")String id, @RequestParam("userName")String userName ){

    }


}
