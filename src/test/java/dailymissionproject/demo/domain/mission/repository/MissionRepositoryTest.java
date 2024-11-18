package dailymissionproject.demo.domain.mission.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.common.config.JPAConfig;
import dailymissionproject.demo.common.config.QueryDSLConfig;
import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.MissionRuleRepository;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
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
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static dailymissionproject.demo.domain.mission.repository.QMission.mission;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {JPAConfig.class, QueryDSLConfig.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MissionRuleRepository missionRuleRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    private final Mission missionFixture = MissionObjectFixture.getMissionFixture();
    private final MissionRule missionRuleFixture = MissionObjectFixture.getMissionRuleFixture();
    private final User userFixture = MissionObjectFixture.getUserFixture();
    private final Long userId = 1L;

    @BeforeEach
    void setUp(){
       queryFactory = new JPAQueryFactory(em);

        // User 더미 데이터 삽입 (10개)
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
        missionRepository.saveAll(missions);
    }

    @Nested
    @DisplayName("미션 조회 레포지토리 테스트")
    class MissionReadRepositoryTest {

        /*
         *  select
         *         m1_0.mission_id,
         *         m1_0.title,
         *         m1_0.content,
         *         m1_0.image_url,
         *         u1_0.nickname,
         *         m1_0.start_date,
         *         m1_0.end_date
         *     from
         *         mission m1_0
         *     join
         *         user u1_0
         *             on u1_0.user_id=m1_0.user_id
         *     where
         *         m1_0.deleted=?
         *         and m1_0.ended=?
         *     order by
         *         (select
         *             count(1)
         *         from
         *             participant p1_0
         *         where
         *             m1_0.mission_id=p1_0.mission_id) desc,
         *         m1_0.created_time desc
         *     limit
         *         ?, ?
       */
        @Test
        @DisplayName("인기 미션 리스트를 조회할 수 있다.")
        void mission_read_hot_list(){
            Pageable pageable = PageRequest.of(0,3);

            List<MissionHotListResponseDto> missionList = queryFactory
                    .select(Projections.fields(MissionHotListResponseDto.class,
                            mission.id,
                            mission.title,
                            mission.content,
                            mission.imageUrl,
                            mission.user.nickname,
                            mission.startDate,
                            mission.endDate,
                            MissionObjectFixture.getParticipantExpression(mission, userId)
                    ))
                    .from(mission)
                    .where(mission.deleted.isFalse().and(mission.ended.isFalse()))
                    .orderBy(mission.participants.size().desc(), mission.createdTime.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            boolean hasNext = false;

            if(missionList.size() > pageable.getPageSize()){
                missionList.remove(pageable.getPageSize());
                hasNext = true;
            }

            Slice<MissionHotListResponseDto> sliceList = new SliceImpl<>(missionList, pageable, hasNext);

            assertThat(sliceList.getContent().size()).isEqualTo(pageable.getPageSize());
        }

        @Test
        @DisplayName("신규 미션 리스트를 조회할 수 있다.")
        void mission_read_new_list(){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime monthBefore = now.minusMonths(10);

            Pageable pageable = PageRequest.of(0,3);

            List<MissionNewListResponseDto> missionList = queryFactory
                    .select(Projections.fields(MissionNewListResponseDto.class,
                            mission.id,
                            mission.title,
                            mission.content,
                            mission.imageUrl,
                            mission.user.nickname,
                            mission.startDate,
                            mission.endDate,
                            MissionObjectFixture.getParticipantExpression(mission, userId)
                    ))
                    .from(mission)
                    .where(mission.createdTime.between(monthBefore, now))
                    .orderBy(mission.createdTime.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            boolean hasNext = false;

            if(missionList.size() > pageable.getPageSize()){
                missionList.remove(pageable.getPageSize());
                hasNext = true;
            }

            Slice<MissionNewListResponseDto> sliceList = new SliceImpl<>(missionList, pageable, hasNext);

            assertThat(sliceList.getContent().size()).isEqualTo(pageable.getPageSize());
            //assertThat(missionList.get(0).getId()).isEqualTo(missionFixture.getId());
        }

        @Test
        @DisplayName("전체 미션 리스트를 조회할 수 있다.")
        void mission_read_all_list(){
            Pageable pageable = PageRequest.of(0,3);

            List<MissionAllListResponseDto> missionList = queryFactory
                    .select(Projections.fields(MissionAllListResponseDto.class,
                            mission.id,
                            mission.title,
                            mission.content,
                            mission.imageUrl,
                            mission.user.nickname,
                            mission.startDate,
                            mission.endDate,
                            mission.ended,
                            MissionObjectFixture.getParticipantExpression(mission, userId)
                    ))
                    .from(mission)
                    .orderBy(mission.createdTime.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            boolean hasNext = false;

            if(missionList.size() > pageable.getPageSize()){
                missionList.remove(pageable.getPageSize());
                hasNext = true;
            }

            Slice<MissionAllListResponseDto> sliceList = new SliceImpl<>(missionList, pageable, hasNext);

            assertThat(sliceList.getContent().size()).isEqualTo(pageable.getPageSize());
        }
    }
}
