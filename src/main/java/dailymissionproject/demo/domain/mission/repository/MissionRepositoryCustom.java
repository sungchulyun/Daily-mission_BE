package dailymissionproject.demo.domain.mission.repository;

import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionEndedListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionHotListResponseDto;
import dailymissionproject.demo.domain.mission.dto.response.MissionNewListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MissionRepositoryCustom {

    Slice<MissionHotListResponseDto> findAllByParticipantSize(Pageable pageable, Long userId);
    Slice<MissionNewListResponseDto> findAllByCreatedInMonth(Pageable pageable, Long userId);
    Slice<MissionAllListResponseDto> findAllByCreatedDate(Pageable pageable, Long userId);
    Slice<MissionEndedListResponseDto> findAllByEnded(Pageable pageable, Long userId);
    List<Mission> findAllByCreatedDate();

    Page<Mission> findAllAndEndedIsFalse(Pageable pageable);
}
