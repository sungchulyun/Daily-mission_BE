package dailymissionproject.demo.domain.participant.repository;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.user.repository.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantRepositoryCustom {
    Optional<Participant> findByMissionAndUser(Mission mission, User user);

    List<Participant> findAllByMission(Mission mission);

    List<Participant> findAllByUser(User user);

}
