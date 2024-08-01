package dailymissionproject.demo.domain.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.auth.exception.AuthException;
import dailymissionproject.demo.domain.auth.exception.AuthExceptionCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static dailymissionproject.demo.domain.auth.exception.AuthExceptionCode.*;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String exception = String.valueOf(request.getAttribute("exception"));

        if(exception == null){
            setResponse(response, TOKEN_NOT_EXIST);
        } else if(exception.equals(EXPIRED_TOKEN.name())){
            setResponse(response, EXPIRED_TOKEN);
        } else if(exception.equals(INVALID_TOKEN.name())){
            setResponse(response, INVALID_TOKEN);
        } else {
            setResponse(response, INVALID_TOKEN);
        }
    }
    private void setResponse(HttpServletResponse response, AuthExceptionCode authExceptionCode) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset-UTF-8");
        response.setStatus(authExceptionCode.getStatus().value());

        GlobalResponse globalResponse = GlobalResponse.fail(authExceptionCode.getMessage());
        String result = new ObjectMapper().writeValueAsString(globalResponse);
        response.getWriter().write(result);
    }
}
