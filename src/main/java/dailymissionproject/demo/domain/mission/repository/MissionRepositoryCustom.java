package dailymissionproject.demo.domain.mission.repository;

import java.util.List;

public interface MissionRepositoryCustom {

    //Hot 미션 목록
    List<Mission> findAllByParticipantSize();

    //New 미션 목록
    List<Mission> findAllByCreatedInMonth();

    //전체 미션 목록
    List<Mission> findAllByCreatedDate();
}
