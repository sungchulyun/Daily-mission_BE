package dailymissionproject.demo.domain.user.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "사용자", description = "유저 관련 API 입니다.")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /*
    @GetMapping("/login")
    @Operation(summary = "로그인 페이지 이동", description = "로그인 요청 시 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public void login(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("https://daily-mission.leey00nsu.com/sign-in/");
    }


     */
    @GetMapping("/home")
    @Operation(summary = "사용자 개인 정보 확인", description = "사용자가 프로필 정보를 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public void home(HttpServletResponse httpServletResponse) throws IOException {

        //httpServletResponse.sendRedirect("https://daily-mission.leey00nsu.com/sign-in/callback");
    }

    @GetMapping("/detail")
    @Operation(summary = "사용자 개인 정보 확인", description = "사용자가 프로필 정보를 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })

    public ResponseEntity<GlobalResponse> getUser(@CurrentUser CustomOAuth2User user){

        return ResponseEntity.ok(success(userService.detail(user)));
    }

    @PutMapping("/profile")
    @Operation(summary = "사용자 개인 정보 업데이트", description = "사용자가 프로필 정보를 수정하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity<GlobalResponse> update(@CurrentUser CustomOAuth2User user
                                                , @RequestBody UserUpdateRequestDto requestDto)throws IOException {

        return ResponseEntity.ok(success(userService.updateProfile(user, requestDto)));
    }

    //유저 마이페이지에서 참여중인 미션, 참여했는데 종료된 미션, 제출한 포스트 목록 무한스크롤
    /*
    * 2024-07-09
    * OAuth2를 통한 로그인으로 변경
     */

    /*
    @PostMapping("/join")
    public UserResDto signUp(@RequestParam("file")MultipartFile file, @RequestBody UserReqDto userReqDto){
        return userService.join(userReqDto);
    }

    @PostMapping("/update/{name}")
    public void updateImg(@PathVariable("name") String name, @RequestParam("file")MultipartFile file)throws IOException {
        userService.updateProfile(name, file);
    }

    @GetMapping("/getUserInfo/{name}")
    public UserResDto getUser(@PathVariable("name")String name){
        return userService.getUserInfo(name);
    }
   */


}