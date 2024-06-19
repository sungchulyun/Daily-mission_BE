package dailymissionproject.demo.domain.mission.controller;

import dailymissionproject.demo.domain.mission.Service.MissionService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
public class MissionController {
    private final MissionService missionService;

    //== 미션 조회 ==//
    @GetMapping("/getInfo/{id}")
    public MissionResponseDto findById(@PathVariable Long id){
        return missionService.findById(id);
    }

    //== 미션 생성==//
    @PostMapping("/create/{userName}")
    public MissionSaveResponseDto save(@PathVariable("userName")String userName, MissionSaveRequestDto missionReqDto){
        return missionService.save(userName, missionReqDto);
    }


    //==Hot 미션 목록 가져오기==//
    @GetMapping("/getInfo/hot")
    public List<MissionHotListResponseDto> findHotList(){

    }

    //==New 미션 목록 가져오기==//
    @GetMapping("/getInfo/new")
    public List<MissionNewListResponseDto> findNewList(){

    }

    //==모든 미션 목록 가져오기==//
    @GetMapping("/getInfo/all")
    public List<MissionAllListResponseDto> findAllList(){

    }


}
