package dailymissionproject.demo.domain.user.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
import dailymissionproject.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Tag(name = "사용자", description = "유저 관련 API 입니다.")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/detail")
    @Operation(summary = "사용자 개인 정보 확인", description = "사용자가 프로필 정보를 확인하고 싶을 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public UserResDto getUser(@AuthenticationPrincipal CustomOAuth2User user){
        return userService.getUserInfo(user.getUsername());
    }
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