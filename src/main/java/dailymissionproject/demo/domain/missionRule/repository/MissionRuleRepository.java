package dailymissionproject.demo.domain.missionRule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRuleRepository extends JpaRepository<MissionRule, Long> {
}
