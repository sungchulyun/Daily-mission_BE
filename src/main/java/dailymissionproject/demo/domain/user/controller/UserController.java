package dailymissionproject.demo.domain.user.controller;

import dailymissionproject.demo.domain.user.dto.request.UserReqDto;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/user/join")
    public String signIn(@RequestBody UserReqDto userReqDto){

        User user = userReqDto.toEntity();
        System.out.println(userReqDto.getName());
        userService.join(user);
        return "success";
    }
}
