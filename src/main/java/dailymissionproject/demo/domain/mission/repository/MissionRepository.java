package dailymissionproject.demo.domain.mission.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {

        Mission save(Mission mission);

        Optional<Mission> findByIdAndDeletedIsFalse(Long id);
}
