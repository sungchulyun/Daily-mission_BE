package dailymissionproject.demo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTestSupport {
    protected static final Logger log = LogManager.getLogger(IntegrationTestSupport.class);
    protected static MariaDBContainer<?> MARIADB_CONTAINER;

    static {
        MARIADB_CONTAINER = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.11"))
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("root");

                MARIADB_CONTAINER.start();
    }

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;


    /*
    @Container
    public static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.11")
            .withReuse(false);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mariaDBContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> mariaDBContainer.getUsername());
        registry.add("spring.datasource.password", () -> mariaDBContainer.getPassword());
    }

     */
}
