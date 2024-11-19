package dailymissionproject.demo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.auth.jwt.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class IntegrationTestSupport {
    protected static final Logger log = LogManager.getLogger(IntegrationTestSupport.class);
    protected static MariaDBContainer<?> MARIADB_CONTAINER;

     static {
        MARIADB_CONTAINER = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.11"))
                .withDatabaseName("dailymission")
                .withUsername("root")
                .withPassword("root");

                MARIADB_CONTAINER.start();
    }

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JWTUtil jwtUtil;
    @Autowired
    private DataInitializer dataInitializer;

    @AfterEach
    void deleteAll(){
        log.info("데이터 초기화 dataInitializer.deleteAll() 시작");
        dataInitializer.deleteAll();
        log.info("데이터 초기화 dataInitializer.deleteAll() 종료");
    }
}
