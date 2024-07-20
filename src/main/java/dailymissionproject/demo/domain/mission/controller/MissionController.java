package dailymissionproject.demo.domain.mission.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.Service.MissionService;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "미션", description = "미션 관련 API 입니다.")
@RequestMapping("/api/mission")
@Slf4j
public class MissionController {

    private final MissionService missionService;

    //== 미션 생성==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/save")
    //@Caching(evict = {
    //        @CacheEvict(value = "missionLists", key = "'hot'"),
    //        @CacheEvict(value = "missionLists", key = "'new'"),
    //        @CacheEvict(value = "missionLists", key = "'all'"),
    //        @CacheEvict(value = "mission", key = "'info'")
    //})
    @Operation(summary = "미션 생성", description = "사용자가 미션을 생성하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "미션 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public MissionSaveResponseDto save(@AuthenticationPrincipal CustomOAuth2User user
                                        , @RequestPart MissionSaveRequestDto missionReqDto
                                        , @RequestPart MultipartFile file) throws IOException {
        return missionService.save(user.getUsername(), missionReqDto, file);
    }

    //== 미션 상세 조회 ==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    @Cacheable(value = "mission", key = "'info'")
    @Operation(summary = "미션 상세 정보 확인", description = "각 미션에 대한 상세정보를 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 미션이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public MissionResponseDto findById(@PathVariable Long id){
        return missionService.findById(id);
    }

    /*
    *미션 삭제
    * 방장만 삭제 가능
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @CacheEvict(value = "mission", key = "'info'")
    @Operation(summary = "미션 삭제", description = "사용자가 미션을 삭제하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 미션이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public boolean delete(@PathVariable("id")Long id, @AuthenticationPrincipal CustomOAuth2User user){
        return missionService.delete(id, user.getUsername());
    }


    //==Hot 미션 목록 가져오기==//
    @GetMapping("/hot")
    //@Cacheable(value = "missionLists", key = "'hot'")
    @Operation(summary = "인기 미션 확인", description = "인기 미션 목록을 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public List<MissionHotListResponseDto> findHotList(Pageable pageable){
        return missionService.findHotList(pageable);
    }


    //==New 미션 목록 가져오기==//
    @GetMapping("/new")
    //@CacheEvict(value = "missionLists", key = "'new'")
    @Operation(summary = "신규 미션 확인", description = "신규 미션 목록을 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public List<MissionNewListResponseDto> findNewList(Pageable pageable){
        return missionService.findNewList(pageable);
    }

    //==모든 미션 목록 가져오기==//
    @GetMapping("/all")
    //@CacheEvict(value = "missionLists", key = "'all'")
    @Operation(summary = "모든 미션 확인", description = "모든 미션 목록을 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public List<MissionAllListResponseDto> findAllList(Pageable pageable){
        return missionService.findAllList(pageable);
    }

}
