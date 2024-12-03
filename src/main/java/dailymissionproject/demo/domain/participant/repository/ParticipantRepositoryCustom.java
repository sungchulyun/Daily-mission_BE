package dailymissionproject.demo.domain.participant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParticipantRepositoryCustom {

    Page<Participant> findAllAndBannedIsFalse(Pageable pageable);
}
