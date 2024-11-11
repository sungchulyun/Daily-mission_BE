package dailymissionproject.demo.domain.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.common.config.JPAConfig;
import dailymissionproject.demo.common.config.QueryDSLConfig;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.MissionRuleRepository;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.post.dto.response.PostMissionListResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUserListResponseDto;
import dailymissionproject.demo.domain.post.fixture.PostObjectFixture;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static dailymissionproject.demo.domain.post.repository.QPost.post;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {JPAConfig.class, QueryDSLConfig.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private MissionRuleRepository missionRuleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private PostRepository postRepository;

    private User findUser;
    private Mission findMission;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);

        List<User> users = List.of(
                User.builder().name("윤성철").email("proattacker@naver.com").nickname("sungchul").username("윤성철").imageUrl("https://aws.s3.bucket.com/image1.png").role(Role.USER).build(),
                User.builder().name("김영희").email("younghee1@example.com").nickname("young1").username("younghee1").imageUrl("http://example.com/image1.png").role(Role.USER).build(),
                User.builder().name("박철수").email("chulsoo2@example.com").nickname("chul2").username("chulsoo2").imageUrl("http://example.com/image2.png").role(Role.USER).build(),
                User.builder().name("최지혜").email("jihye3@example.com").nickname("ji3").username("jihye3").imageUrl("http://example.com/image3.png").role(Role.USER).build(),
                User.builder().name("이민호").email("minho4@example.com").nickname("minho4").username("minho4").imageUrl("http://example.com/image4.png").role(Role.USER).build(),
                User.builder().name("정수빈").email("soobin5@example.com").nickname("soo5").username("soobin5").imageUrl("http://example.com/image5.png").role(Role.USER).build(),
                User.builder().name("한상우").email("sangwoo6@example.com").nickname("sang6").username("sangwoo6").imageUrl("http://example.com/image6.png").role(Role.USER).build(),
                User.builder().name("오세훈").email("sehun7@example.com").nickname("sehun7").username("sehun7").imageUrl("http://example.com/image7.png").role(Role.USER).build(),
                User.builder().name("임채원").email("chae8@example.com").nickname("chae8").username("chae8").imageUrl("http://example.com/image8.png").role(Role.USER).build(),
                User.builder().name("강민재").email("minjae9@example.com").nickname("minjae9").username("minjae9").imageUrl("http://example.com/image9.png").role(Role.USER).build()
        );

        findUser = users.get(0);

        userRepository.saveAll(users);

        // MissionRule 더미 데이터 삽입 (10개)
        List<MissionRule> missionRules = List.of(
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false)),
                new MissionRule(new Week(true, true, true, true, true, false, false))
        );
        missionRuleRepository.saveAll(missionRules);

        // Mission 더미 데이터 삽입 (10개)
        List<Mission> missions = List.of(
                Mission.builder().missionRule(missionRules.get(0)).user(users.get(0)).title("1일 1식 도전").content("하루에 한끼만 먹기 도전").imageUrl("http://example.com/mission1.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(5)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(1)).user(users.get(1)).title("소소한 저축").content("소소하게 돈을 저축해요").imageUrl("http://example.com/mission2.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(4)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(2)).user(users.get(2)).title("독서 습관 기르기").content("매일 30분 독서하기").imageUrl("http://example.com/mission3.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(3)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(3)).user(users.get(3)).title("운동 루틴 만들기").content("매일 30분 운동하기").imageUrl("http://example.com/mission4.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(2)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(4)).user(users.get(4)).title("매일 감사하기").content("매일 감사한 일 적기").imageUrl("http://example.com/mission5.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(1)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(5)).user(users.get(5)).title("사진 찍기").content("하루에 한 장의 사진 찍기").imageUrl("http://example.com/mission6.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusDays(5)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(6)).user(users.get(6)).title("내일의 계획 세우기").content("매일 하루를 계획하기").imageUrl("http://example.com/mission7.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusDays(3)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(7)).user(users.get(7)).title("아침 일찍 일어나기").content("매일 아침 6시에 일어나기").imageUrl("http://example.com/mission8.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusDays(5)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(8)).user(users.get(8)).title("영화 감상").content("주말마다 영화 한 편 보기").imageUrl("http://example.com/mission9.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(3)).endDate(LocalDate.now().plusMonths(3)).build(),
                Mission.builder().missionRule(missionRules.get(9)).user(users.get(9)).title("요리 연습하기").content("매일 새로운 요리 시도하기").imageUrl("http://example.com/mission10.png").hint("HINT").credential("CREDENTIAL").startDate(LocalDate.now().minusMonths(4)).endDate(LocalDate.now().plusMonths(3)).build()
        );
        findMission = missions.get(0);
        missionRepository.saveAll(missions);

        List<Post> posts = List.of(
                Post.builder().user(users.get(0)).mission(missions.get(0)).title("TITLE1").content("CONTENT1").imageUrl("IMAGE1").build(),
                Post.builder().user(users.get(1)).mission(missions.get(0)).title("TITLE2").content("CONTENT2").imageUrl("IMAGE2").build(),
                Post.builder().user(users.get(1)).mission(missions.get(1)).title("TITLE3").content("CONTENT3").imageUrl("IMAGE3").build(),
                Post.builder().user(findUser).mission(missions.get(1)).title("TITLE4").content("CONTENT4").imageUrl("IMAGE4").build()
        );

        postRepository.saveAll(posts);
    }

    @Nested
    @DisplayName("포스트 조회 레포지토리 테스트")
    class PostReadRepositoryTest {

        @Test
        @DisplayName("전체 포스트를 조회할 수 있다.")
        @Transactional()
        void post_read_all_success() throws Exception {
            List<Post> posts = queryFactory
                    .select(post)
                    .from(post)
                    .where(post.deleted.isFalse())
                    .orderBy(post.createdDate.desc())
                    .fetch();

            assertThat(posts.size()).isEqualTo(4);
        }

        @Test
        @DisplayName("유저별 포스트를 조회할 수 있다.")
        @Transactional
        void post_read_user_list_success() throws Exception {
            Pageable pageable = PageRequest.of(0, 3);

            List<PostUserListResponseDto> list = queryFactory.select(Projections.constructor(PostUserListResponseDto.class,
                            post.id,
                            post.mission.id,
                            post.mission.title,
                            post.title,
                            post.content,
                            post.imageUrl,
                            post.createdTime,
                            post.modifiedDate))
                    .from(post)
                    .where(post.user.eq(findUser).and(post.deleted.isFalse()))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(post.modifiedDate.desc())
                    .fetch();

            assertThat(list.size()).isEqualTo(2);
            assertThat(list.get(0).getTitle()).isEqualTo("TITLE4");
            assertThat(list.get(1).getTitle()).isEqualTo("TITLE1");
            assertThat(list.get(0).getModifiedDate()).isAfterOrEqualTo(list.get(1).getModifiedDate());
            assertThat(list.size()).isLessThanOrEqualTo(pageable.getPageSize());
        }

        @Test
        @DisplayName("미션별 포스트 리스트를 조회할 수 있다.")
        @Transactional
        void post_read_mission_list_success() throws Exception {
            Pageable pageable = PageRequest.of(0, 3);

            List<PostMissionListResponseDto> list = queryFactory
                    .select(Projections.fields(PostMissionListResponseDto.class,
                            post.id,
                            post.mission.id.as("missionId"),
                            post.title,
                            post.content,
                            post.imageUrl,
                            post.createdDate,
                            post.modifiedDate))
                    .from(post)
                    .where(post.mission.eq(findMission).and(post.deleted.isFalse()))
                    .orderBy(post.modifiedDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            assertThat(list.size()).isEqualTo(2);
            assertThat(list.get(0).getTitle()).isEqualTo("TITLE2");
            assertThat(list.get(1).getTitle()).isEqualTo("TITLE1");
            assertThat(list.get(0).getModifiedDate()).isAfterOrEqualTo(list.get(1).getModifiedDate());
            assertThat(list.size()).isLessThanOrEqualTo(pageable.getPageSize());
        }

        @DisplayName("금일 포스트 제출 유무를 검증할 수 있다.")
        @Test
        void post_read_submitted_today_success(){
            LocalDateTime startDate = LocalDateTime.now().minusMinutes(5);
            LocalDateTime endDate = startDate.plusDays(1);

            Long postCount = queryFactory
                    .select(post.countDistinct())
                    .from(post)
                    .where(post.mission.eq(findMission)
                            .and(post.user.eq(findUser))
                            .and(post.createdDate.after(startDate))
                            .and(post.createdDate.before(endDate))
                            .and(post.deleted.isFalse()))
                    .fetchOne();

            assertThat(postCount).isEqualTo(1);
        }
    }
}
