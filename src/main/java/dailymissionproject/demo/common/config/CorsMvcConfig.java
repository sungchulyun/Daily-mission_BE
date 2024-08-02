package dailymissionproject.demo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){

        corsRegistry.addMapping("/api/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("http://localhost:3000", "https://daily-mission.leey00nsu.site", "https://daily-mission.site")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}
