package dailymissionproject.demo.domain.post.fixture;

import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;

public class PostObjectFixture {

    /**
     * 유저 엔티티 fixture를 반환한다.
     * @return User
     */
    public static User getUserFixture(){
        return User.builder()
                .username("google 1923819273")
                .email("google@gmail.com")
                .nickname("sungchul")
                .imageUrl("https://aws-s3.jpg")
                .name("윤성철")
                .role(Role.USER)
                .build();
    }

    /**
     * 미션규칙 엔티티 fixture를 반환합니다.
     * @return MissionRule
     */
    public static MissionRule getMissionRuleFixture(){
        return MissionRule.builder()
                .week(new Week(false, true, true, true, true, true, false))
                .build();
    }

    /**
     * Mission 엔티티 fixture를 반환한다.
     * @return Mission
     */
    public static Mission getMissionFixture(){
        return Mission.builder()
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("THUMBNAIL.jpg")
                .hint("HINT")
                .credential("CREDENTIAL")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .user(getUserFixture())
                .missionRule(getMissionRuleFixture())
                .build();
    }

    /**
     * Post 엔티티 fixture를 반환한다.
     * @return Post
     */
    public static Post getPostFixture(){
        return Post.builder()
                .mission(getMissionFixture())
                .user(getUserFixture())
                .title("TITLE")
                .content("CONTENT")
                .imgUrl("IMAGE")
                .build();
    }

    /**
     * Post 생성 요청 fixture를 반환한다.
     * @return PostSaveRequestDto
     */
    public static PostSaveRequestDto getSaveRequest(){
        return PostSaveRequestDto.builder()
                .missionId(1L)
                .title("TITLE")
                .content("CONTENT")
                .build();
    }

    /**
     * Post 상세조회 fixture를 반환한다.
     * @return PostResponseDto
     */
    public static PostResponseDto getDetailResponse(){
        return PostResponseDto.builder()
                .post(getPostFixture())
                .build();
    }
}
