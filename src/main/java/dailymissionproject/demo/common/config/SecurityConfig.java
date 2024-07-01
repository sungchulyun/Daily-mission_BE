package dailymissionproject.demo.common.config;

import dailymissionproject.demo.domain.auth.jwt.JWTFilter;
import dailymissionproject.demo.domain.auth.jwt.JWTUtil;
import dailymissionproject.demo.domain.auth.oauth2.CustomSuccessHandler;
import dailymissionproject.demo.domain.auth.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    //private final CustomOAuth2UserService customOAuth2UserService;

    //public SecurityConfig(CustomOAuth2UserService customOAuth2UserService){
    //    this.customOAuth2UserService = customOAuth2UserService;
    //}
    @Bean
    public SecurityFilterChain config(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

       //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //Http Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //JWT 필터 추가
        http
                .addFilterAfter(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customSuccessHandler)
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

        /*
        http

               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement((sessionManagement) ->
                       sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                       )
               .formLogin(AbstractHttpConfigurer::disable)
               .httpBasic(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                       .requestMatchers(new MvcRequestMatcher(introspector, "/")).permitAll()
               )
               .oauth2Login(oauth2Login ->
                       oauth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                       userInfoEndpointConfig.userService(customOAuth2UserService)))
               ;
       return http.build();

        */
    }
}
