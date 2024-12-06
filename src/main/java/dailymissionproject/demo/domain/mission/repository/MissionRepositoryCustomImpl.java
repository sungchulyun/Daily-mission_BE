package dailymissionproject.demo.domain.mission.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import dailymissionproject.demo.domain.participant.repository.QParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static dailymissionproject.demo.domain.mission.repository.QMission.mission;
import static dailymissionproject.demo.domain.participant.repository.QParticipant.participant;

@RequiredArgsConstructor
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    /**
     * 인기 미션리스트를 반환한다.
     * @param pageable
     * @return
     */
    public Slice<MissionHotListResponseDto> findAllByParticipantSize(Pageable pageable, Long userId){
        List<MissionHotListResponseDto> missionList = fetchByParticipantSize(pageable, userId);
        boolean hasNext = hasNextPage(missionList, pageable);

        return new SliceImpl<>(missionList, pageable, hasNext);
    }

    public List<MissionHotListResponseDto> fetchByParticipantSize(Pageable pageable, Long userId){
        return queryFactory
                .select(Projections.fields(MissionHotListResponseDto.class,
                        mission.id,
                        mission.title,
                        mission.content,
                        mission.imageUrl,
                        mission.user.nickname,
                        mission.startDate,
                        mission.endDate
                ))
                .from(mission)
                .where(mission.deleted.isFalse().and(mission.ended.isFalse()))
                .orderBy(mission.participants.size().desc(), mission.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    /**
     * 신규 미션리스트를 반환한다.
     * @param pageable
     * @return
     */
    public Slice<MissionNewListResponseDto> findAllByCreatedInMonth(Pageable pageable, Long userId){
        List<MissionNewListResponseDto> missionList = fetchMissionByCreatedInMonth(pageable, userId);
        boolean hasNext = hasNextPage(missionList, pageable);

        return new SliceImpl<>(missionList, pageable, hasNext);
    }

    public List<MissionNewListResponseDto> fetchMissionByCreatedInMonth(Pageable pageable, Long userId){
       return queryFactory
                .select(Projections.fields(MissionNewListResponseDto.class,
                        mission.id,
                        mission.title,
                        mission.content,
                        mission.imageUrl,
                        mission.user.nickname,
                        mission.startDate,
                        mission.endDate
                ))
                .from(mission)
                .where(mission.deleted.isFalse().and(mission.ended.isFalse()))
                .orderBy(mission.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    /**
     * 전체 미션리스트를 반환한다.
     * @param pageable
     * @return
     */
    public Slice<MissionAllListResponseDto> findAllByCreatedDate(Pageable pageable, Long userId){
        List<MissionAllListResponseDto> missionList = fetchMissionByCreatedDate(pageable, userId);
        boolean hasNext = hasNextPage(missionList, pageable);

        return new SliceImpl<>(missionList, pageable, hasNext);
    }

    public List<MissionAllListResponseDto> fetchMissionByCreatedDate(Pageable pageable, Long userId){

       return queryFactory
                .select(Projections.fields(MissionAllListResponseDto.class,
                        mission.id,
                        mission.title,
                        mission.content,
                        mission.imageUrl,
                        mission.user.nickname,
                        mission.startDate,
                        mission.endDate,
                        mission.ended
                        ))
                .from(mission)
                .orderBy(mission.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    /**
     * Pageable 요청객체의 사이즈와 비교해서 크다면, true를 리턴한다.
     * @param missionList
     * @param pageable
     * @return
     */
    private boolean hasNextPage(List<?> missionList, Pageable pageable){
        if (missionList.size() > pageable.getPageSize()) {
            missionList.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }

    @Override
    public List<Mission> findAllByCreatedDate() {
        return queryFactory
                .select(mission)
                .from(mission)
                .orderBy(mission.createdTime.desc())
                .fetch();
    }

    @Override
    public Page<Mission> findAllAndEndedIsFalse(Pageable pageable) {
        List<Mission> missions =  queryFactory
                .select(mission)
                .from(mission)
                .where(mission.ended.isFalse())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(missions, pageable, missions.size());
    }

}
