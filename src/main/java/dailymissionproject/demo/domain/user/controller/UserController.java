package dailymissionproject.demo.domain.user.controller;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
import dailymissionproject.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Tag(name = "사용자 조회 API", description = "User컨트롤러에 대한 설명입니다.")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

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

    @GetMapping("/info")
    @Cacheable(value = "user", key = "#user.username")
    public UserResDto getUser(@AuthenticationPrincipal CustomOAuth2User user){
        return userService.getUserInfo(user.getUsername());
    }
}