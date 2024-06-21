package dailymissionproject.demo.domain.mission.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static dailymissionproject.demo.domain.mission.repository.QMission.mission;

@RequiredArgsConstructor
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Mission> findAllByParticipantSize() {
        return queryFactory
                .select(mission)
                .from(mission)
                .where(mission.deleted.isFalse().and(mission.ended.isFalse()))
                .orderBy(mission.participants.size().desc(), mission.createdTime.desc())
                .fetch();
    }

    @Override
    public List<Mission> findAllByCreatedInMonth() {
        return queryFactory
                .select(mission)
                .from(mission)
                .where(mission.deleted.isFalse(), mission.ended.isFalse())
                .orderBy(mission.createdTime.desc())
                .fetch();
    }

    @Override
    public List<Mission> findAllByCreatedDate() {
        return queryFactory
                .select(mission)
                .from(mission)
                .orderBy(mission.createdTime.desc())
                .fetch();
    }
}
