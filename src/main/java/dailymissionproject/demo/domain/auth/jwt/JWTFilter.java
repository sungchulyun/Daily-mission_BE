package dailymissionproject.demo.domain.auth.jwt;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.auth.exception.AuthExceptionCode;
import dailymissionproject.demo.domain.user.repository.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestUri = request.getRequestURI();
        if(requestUri.matches("^\\/login(?:\\/.*)?$")){

            filterChain.doFilter(request, response);
            return;
        }

        if(requestUri.matches("^\\/oauth2(?:\\/.*)?$")){
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = "";
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){

            if(cookie.getName().equals("Authorization")){

                authorization = cookie.getValue();
            }
        }

        if(authorization == null){
            log.info("token is null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        try{
            if(jwtUtil.isExpired(token)){
                log.info("token is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setRole("ROLE_USER");

            //인증 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

            if(!jwtUtil.validToken(token, customOAuth2User)){
                log.info("token is invalid");
                filterChain.doFilter(request, response);
                return;
            }

            //시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            request.setAttribute("exception", AuthExceptionCode.EXPIRED_TOKEN.name());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }



}
