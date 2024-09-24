package dailymissionproject.demo.domain.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.mission.service.MissionService;
import dailymissionproject.demo.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@Tag("unit")
@DisplayName("[unit] [controller] MissionController")
@WebMvcTest(MissionController.class)
@WithMockCustomUser
class MissionControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private MissionService missionService;

    private final Long missionId = 1L;

    @Nested
    @DisplayName("미션 조회 컨트롤러 테스트")
    class MissionReadControllerTest {

        @Test
        @DisplayName("미션 상세정보를 조회할 수 있다.")
        void mission_detail_read_test() throws Exception {

           // when(missionService.findById(missionId)).thenReturn()
        }
    }
}
