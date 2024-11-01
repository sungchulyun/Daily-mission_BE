package dailymissionproject.demo.domain.post.fixture;

import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.post.dto.request.PostSaveRequestDto;
import dailymissionproject.demo.domain.post.dto.request.PostUpdateRequestDto;
import dailymissionproject.demo.domain.post.dto.response.PostResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUpdateResponseDto;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * 유저가 작성한 포스트 리스트 fixture를 반환한다.
     * @return
     */
    public static List<PostResponseDto> getUserPostList(){
        User user =  new User(1L, "윤성철", "google@gamil.com", "sungchul");

        Post post_1 = Post.builder()
                .user(user)
                .mission(getMissionFixture())
                .title("TITLE1")
                .content("CONTENT1")
                .imgUrl("IMAGE1")
                .build();

        Post post_2 = Post.builder()
                .user(user)
                .mission(getMissionFixture())
                .title("TITLE2")
                .content("CONTENT2")
                .imgUrl("IMAGE2")
                .build();

        PostResponseDto postResponse_1 = PostResponseDto.builder()
                .post(post_1)
                .build();

        PostResponseDto postResponse_2 = PostResponseDto.builder()
                .post(post_2)
                .build();

        return List.of(postResponse_1, postResponse_2);
    }

    /**
     * 미션별로 작성된 포스트 리스트를 반환한다.
     * @return
     */
    public static List<PostResponseDto> getMissionPostList(){
        Mission mission = new Mission(1L, getUserFixture(), "MISSION_TITLE", "MISSION_CONTENT"
                , "IMAGEURL", LocalDate.now(), LocalDate.now());

        Post post_1 = Post.builder()
                .user(getUserFixture())
                .mission(mission)
                .title("TITLE1")
                .content("CONTENT1")
                .imgUrl("IMAGE1")
                .build();

        Post post_2 = Post.builder()
                .user(getUserFixture())
                .mission(mission)
                .title("TITLE2")
                .content("CONTENT2")
                .imgUrl("IMAGE2")
                .build();

        PostResponseDto postResponse_1 = PostResponseDto.builder()
                .post(post_1)
                .build();

        PostResponseDto postResponse_2 = PostResponseDto.builder()
                .post(post_2)
                .build();

        return List.of(postResponse_1, postResponse_2);
    }

    public static PostUpdateRequestDto getPostUpdateRequest(){
        return PostUpdateRequestDto.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
    }

    public static PostUpdateResponseDto getPostUpdateResponse(){
        return PostUpdateResponseDto.builder()
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("IMAGE")
                .build();
    }
}
