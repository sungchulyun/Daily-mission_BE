package dailymissionproject.demo.domain.notify.controller;


import dailymissionproject.demo.common.repository.CurrentUser;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.notify.service.NotifyService;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notify")
public class NotifyController {

    private final NotifyService notifyService;
    private final UserRepository userRepository;

    @GetMapping(value = "subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@CurrentUser CustomOAuth2User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        return notifyService.subscribe(findUser.getId());
    }
}
