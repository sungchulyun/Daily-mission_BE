package dailymissionproject.demo.domain.mission.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MissionRepositoryCustom {

    //== Pagination 적용 ==//
    //Hot 미션 목록
    Slice<Mission> findAllByParticipantSize(Pageable pageable);

    //New 미션 목록
    Slice<Mission> findAllByCreatedInMonth(Pageable pageable);

    //All 미션 목록
    Slice<Mission> findAllByCreatedDate(Pageable pageable);




    //List<Mission> findAllByParticipantSize();
    //List<Mission> findAllByCreatedInMonth();

    //List<Mission> findAllByCreatedDate();
}
