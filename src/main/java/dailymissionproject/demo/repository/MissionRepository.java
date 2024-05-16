package dailymissionproject.demo.repository;

import dailymissionproject.demo.entity.Mission;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class MissionRepository {

    private final EntityManager em;


    public void save(Mission mission){
        em.persist(mission);
    }
}
