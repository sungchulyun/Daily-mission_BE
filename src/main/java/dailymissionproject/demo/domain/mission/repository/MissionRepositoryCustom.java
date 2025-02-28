package dailymissionproject.demo.domain.mission.repository;

import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionUserListResponseDto;
import org.springframework.data.domain.Page;
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

    Page<Mission> findAllAndEndedIsFalse(Pageable pageable);

    List<MissionUserListResponseDto> findMissionDtoByUser(Long userId);
}
