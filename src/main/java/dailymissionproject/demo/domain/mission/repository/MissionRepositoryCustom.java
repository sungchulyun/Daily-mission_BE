package dailymissionproject.demo.domain.mission.repository;

import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MissionRepositoryCustom {

    //== Pagination 적용 ==//
    //Hot 미션 목록
    Slice<MissionHotListResponseDto> findAllByParticipantSize(Pageable pageable, Long userId);

    //New 미션 목록
    Slice<MissionNewListResponseDto> findAllByCreatedInMonth(Pageable pageable, Long userId);

    //All 미션 목록
    Slice<MissionAllListResponseDto> findAllByCreatedDate(Pageable pageable, Long userId);
    List<Mission> findAllByCreatedDate();
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



    //List<Mission> findAllByParticipantSize();
    //List<Mission> findAllByCreatedInMonth();

    //List<Mission> findAllByCreatedDate();
}
