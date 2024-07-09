package dailymissionproject.demo.domain.user.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.sun.security.auth.UserPrincipal;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.user.dto.request.UserReqDto;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
import dailymissionproject.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/user")
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