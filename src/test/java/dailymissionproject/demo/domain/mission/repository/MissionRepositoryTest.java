package dailymissionproject.demo.domain.mission.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.common.config.JPAConfig;
import dailymissionproject.demo.common.config.QueryDSLConfig;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.fixture.MissionObjectFixture;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
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

import java.util.List;

import static dailymissionproject.demo.domain.mission.repository.QMission.mission;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {JPAConfig.class, QueryDSLConfig.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    private final Mission missionFixture = MissionObjectFixture.getMissionFixture();
    private final MissionRule missionRuleFixture = MissionObjectFixture.getMissionRuleFixture();
    private final User userFixture = MissionObjectFixture.getUserFixture();

    @BeforeEach
    void setUp(){

       userRepository.save(userFixture);

       missionFixture.setUser(userFixture);
       missionRepository.save(missionFixture);

       queryFactory = new JPAQueryFactory(em);
    }

    @Nested
    @DisplayName("미션 조회 레포지토리 테스트")
    class MissionReadRepositoryTest {

        /**
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
                            mission.endDate))
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

            assertThat(sliceList.getContent().size()).isEqualTo(1);
            assertThat(sliceList.getContent().get(0).getId()).isEqualTo(missionFixture.getId());
        }
    }
}
