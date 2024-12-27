package dailymissionproject.demo.domain.likes.fixture;

import dailymissionproject.demo.domain.like.dto.response.LikesResponseDto;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;

import java.time.LocalDate;

public class LikesObjectFixture {

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

    public static Mission getMissionFixture(){
        User user = new User(1L, "윤성철", "proattacker@naver.com", "성철");

        return Mission.builder()
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("THUMBNAIL.jpg")
                .hint("HINT")
                .credential("CREDENTIAL")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .user(user)
                .missionRule(MissionRule.builder()
                        .week(new Week(false, false, false, true, true, true, true))
                        .build())
                .build();
    }
    public static Post getPostFixture(){
        return Post.builder()
                .mission(getMissionFixture())
                .user(getUserFixture())
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("IMAGE")
                .build();
    }

    public static LikesResponseDto getLikeResponse(){
        return LikesResponseDto.builder()
                .userId(1L)
                .postId(1L)
                .build();
    }
}
