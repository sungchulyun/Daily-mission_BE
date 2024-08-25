package dailymissionproject.demo.domain.auth.oauth2;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        //Role role = Role.valueOf(auth.getAuthority());

        String role = auth.getAuthority();
        String token = jwtUtil.createJwt(username, role,3600*60*60L);

        //response.addCookie(createCookie("Authorization", token));
        createCookie(response, "Authorization", token);
        response.sendRedirect("https://daily-mission.leey00nsu.com/sign-in/callback");
    }

    public static void createCookie(HttpServletResponse response, String key, String value){

        /*Cookie cookie =  new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
         */

        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(60*60*60)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
