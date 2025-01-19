package dailymissionproject.demo.domain.auth.jwt;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.auth.dto.UserDto;
import dailymissionproject.demo.domain.auth.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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
import java.util.Arrays;


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

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            Long id = Long.parseLong(jwtUtil.getUserId(token));

            UserDto userDto = new UserDto();
            userDto.setId(id);
            userDto.setUsername(username);
            userDto.setRole("ROLE_USER");

            //인증 객체 담기
            //CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
            CustomOAuth2User customOAuth2User = CustomOAuth2User.create(userDto);

            if(!jwtUtil.validToken(token, customOAuth2User)){
                log.info("token is invalid");
                request.setAttribute("exception", new MalformedJwtException("토큰이 유효하지 않습니다."));
                filterChain.doFilter(request, response);
                return;
            }

            //시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.warn("잘못된 JWT 서명입니다.");
            request.setAttribute("exception", e);
        } catch (UnsupportedJwtException e){
            log.warn("지원되지 않는 JWT 토큰입니다.");
            request.setAttribute("exception", e);
        } catch (ExpiredJwtException e){
            log.warn("만료된 JWT 토큰입니다.");
            request.setAttribute("exception", e);
        } catch (IllegalArgumentException e){
            log.warn("잘못된 JWT 토큰입니다.");
            request.setAttribute("exception", e);
        } catch (AuthException e){
            log.warn("JWT 토큰이 존재하지 않습니다.");
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/swagger-ui/",
                "/v3/",
                "/swagger-",
                "/error"
        };
        String path = request.getRequestURI();

        boolean shouldNotFilter = Arrays.stream(excludePath).anyMatch(path::startsWith);
        return shouldNotFilter;
    }
}
