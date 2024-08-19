package dailymissionproject.demo.domain.mission.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static dailymissionproject.demo.domain.mission.repository.QMission.mission;

@RequiredArgsConstructor
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    /*
    public Slice<Mission> findAllByParticipantSize(Pageable pageable){
        List<Mission> missionList = queryFactory
                .select(mission)
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
        return new SliceImpl<>(missionList, pageable, hasNext);
    }

     */

    public Slice<MissionHotListResponseDto> findAllByParticipantSize(Pageable pageable){

        List<MissionHotListResponseDto> missionList = queryFactory
                .select(Projections.fields(MissionHotListResponseDto.class,
                        mission.id,
                        mission.title,
                        mission.content,
                        mission.imageUrl,
                        mission.user.name,
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
        return new SliceImpl<>(missionList, pageable, hasNext);
    }

    public Slice<MissionNewListResponseDto> findAllByCreatedInMonth(Pageable pageable){
        List<MissionNewListResponseDto> missionList = queryFactory
                .select(Projections.fields(MissionNewListResponseDto.class,
                        mission.id,
                        mission.title,
                        mission.content,
                        mission.imageUrl,
                        mission.user.name,
                        mission.startDate,
                        mission.endDate))
                .from(mission)
                .where(mission.deleted.isFalse().and(mission.ended.isFalse()))
                .orderBy(mission.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        boolean hasNext = false;
        if(missionList.size() > pageable.getPageSize()){
            missionList.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(missionList, pageable, hasNext);
    }

    public Slice<MissionAllListResponseDto> findAllByCreatedDate(Pageable pageable){
        List<MissionAllListResponseDto> missionList = queryFactory
                .select(Projections.fields(MissionAllListResponseDto.class,
                        mission.id,
                        mission.title,
                        mission.content,
                        mission.imageUrl,
                        mission.user.name,
                        mission.startDate,
                        mission.endDate,
                        mission.ended))
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
        return new SliceImpl<>(missionList, pageable, hasNext);
    }
    /*
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
    */
    @Override
    public List<Mission> findAllByCreatedDate() {
        return queryFactory
                .select(mission)
                .from(mission)
                .orderBy(mission.createdTime.desc())
                .fetch();
    }


}
