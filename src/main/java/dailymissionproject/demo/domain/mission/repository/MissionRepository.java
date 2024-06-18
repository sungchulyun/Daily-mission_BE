package dailymissionproject.demo.domain.mission.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MissionRepository extends JpaRepository<Mission, Long> {

}
