package dailymissionproject.demo.domain.mission.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.Service.MissionService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
@Slf4j
public class MissionController {

    private final MissionService missionService;

    //== 미션 상세 조회 ==//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getInfo/{id}")
    public MissionResponseDto findById(@PathVariable Long id){
        return missionService.findById(id);
    }

    //== 미션 생성==//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public MissionSaveResponseDto save(@RequestPart MissionSaveRequestDto missionReqDto, @RequestPart MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        return missionService.save(user.getUsername(), missionReqDto, file);
    }

    /*
    *미션 삭제
    * 방장만 삭제 가능
     */
    @DeleteMapping("/delete/{id}/{userName}")
    public boolean delete(@PathVariable("id")Long id, @PathVariable("userName")String userName){
        return missionService.delete(id, userName);
    }


    //==Hot 미션 목록 가져오기==//
    @GetMapping("/get/hot")
    public List<MissionHotListResponseDto> findHotList(){
        return missionService.findHotList();
    }


    //==New 미션 목록 가져오기==//
    @GetMapping("/get/new")
    public List<MissionNewListResponseDto> findNewList(){
        return missionService.findNewList();
    }

    //==모든 미션 목록 가져오기==//
    @GetMapping("/get/all")
    public List<MissionAllListResponseDto> findAllList(){
        return missionService.findAllList();
    }

}
