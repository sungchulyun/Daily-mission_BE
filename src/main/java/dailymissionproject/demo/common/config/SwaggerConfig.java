package dailymissionproject.demo.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi(){
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addServersItem(new Server().url("https://daily-mission.site"));
    }

    private Info apiInfo(){
        return new Info()
                .title("Daily-Mission Swagger")
                .description("일일 미션 생성, 참여, 인증글 작성과 관련한 REST API")
                .version("1.0.0");
    }
}
