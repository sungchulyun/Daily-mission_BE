package dailymissionproject.demo.domain.mission.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.meta.MetaService;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;



@RestController
@RequiredArgsConstructor
@Tag(name = "미션", description = "미션 관련 API 입니다.")
@RequestMapping("/api/v1/mission")
@Slf4j
public class MissionController {

    private final MissionService missionService;

    //== 미션 생성==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/save")
    @Operation(summary = "미션 생성", description = "사용자가 미션을 생성하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "미션 생성에 실패하였습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> save(@CurrentUser CustomOAuth2User user
                                                , @RequestPart MissionSaveRequestDto missionReqDto
                                                , @RequestPart MultipartFile file) throws IOException {

        return ResponseEntity.ok(success(missionService.save(user, missionReqDto, file)));
    }

    //== 미션 상세 조회 ==//
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    @Operation(summary = "미션 상세 정보 확인", description = "각 미션에 대한 상세정보를 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 미션이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(success(missionService.findById(id)));
    }

    /*
    *미션 삭제
    * 방장만 삭제 가능
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "미션 삭제", description = "사용자가 미션을 삭제하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 미션이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id")Long id, @CurrentUser CustomOAuth2User user){

        return ResponseEntity.ok(success(missionService.delete(id, user)));
    }


    //==Hot 미션 목록 가져오기==//
    @GetMapping("/hot")
    @Operation(summary = "인기 미션 확인", description = "인기 미션 목록을 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findHotList(Pageable pageable){

        PageResponseDto response = missionService.findHotList(pageable);

        return ResponseEntity.ok(success(response.content(), MetaService.createMetaInfo().add("isNext", response.next())));

    }


    //==New 미션 목록 가져오기==//
    @GetMapping("/new")
    @Operation(summary = "신규 미션 확인", description = "신규 미션 목록을 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findNewList(Pageable pageable){

        PageResponseDto response = missionService.findNewList(pageable);

        return ResponseEntity.ok(success(response.content(), MetaService.createMetaInfo().add("isNext", response.next())));
    }

    //==모든 미션 목록 가져오기==//
    @GetMapping("/all")
    @Operation(summary = "모든 미션 확인", description = "모든 미션 목록을 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> findAllList(Pageable pageable){
        PageResponseDto response = missionService.findAllList(pageable);

        return ResponseEntity.ok(success(response.content(), MetaService.createMetaInfo().add("isNext", response.next())));
    }

}
