package dailymissionproject.demo.controller;

import dailymissionproject.demo.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class loginController {
    private final HttpSession httpSession;
    @GetMapping("/")
    public String hello(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        log.info("userName : {}", user.getName());
        return "index";
    }
}
