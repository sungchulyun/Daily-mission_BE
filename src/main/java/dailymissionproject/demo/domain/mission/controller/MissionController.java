package dailymissionproject.demo.domain.mission.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.Service.MissionService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
@Slf4j
public class MissionController {

    private final MissionService missionService;

    //== 미션 생성==//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @Caching(evict = {
            @CacheEvict(value = "missionLists", key = "'hot'"),
            @CacheEvict(value = "missionLists", key = "'new'"),
            @CacheEvict(value = "missionLists", key = "'all'"),
            @CacheEvict(value = "mission", key = "'info'")
    })
    public MissionSaveResponseDto save(@AuthenticationPrincipal CustomOAuth2User user
            , @RequestPart MissionSaveRequestDto missionReqDto
            , @RequestPart MultipartFile file) throws IOException {
        return missionService.save(user.getUsername(), missionReqDto, file);
    }

    //== 미션 상세 조회 ==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getInfo/{id}")
    @Cacheable(value = "mission", key = "'info'")
    public MissionResponseDto findById(@PathVariable Long id){
        return missionService.findById(id);
    }

    /*
    *미션 삭제
    * 방장만 삭제 가능
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @CacheEvict(value = "mission", key = "'info'")
    public boolean delete(@PathVariable("id")Long id, @AuthenticationPrincipal CustomOAuth2User user){
        return missionService.delete(id, user.getUsername());
    }


    //==Hot 미션 목록 가져오기==//
    @GetMapping("/get/hot")
    @Cacheable(value = "missionLists", key = "'hot")
    public List<MissionHotListResponseDto> findHotList(){
        return missionService.findHotList();
    }


    //==New 미션 목록 가져오기==//
    @GetMapping("/get/new")
    @CacheEvict(value = "missionLists", key = "'new'")
    public List<MissionNewListResponseDto> findNewList(){
        return missionService.findNewList();
    }

    //==모든 미션 목록 가져오기==//
    @GetMapping("/get/all")
    @CacheEvict(value = "missionLists", key = "'all'")
    public List<MissionAllListResponseDto> findAllList(){
        return missionService.findAllList();
    }

}
