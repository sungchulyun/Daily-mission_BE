package dailymissionproject.demo.domain.mission.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {

        Mission save(Mission mission);

        Optional<Mission> findByIdAndDeletedIsFalse(Long id);

}
