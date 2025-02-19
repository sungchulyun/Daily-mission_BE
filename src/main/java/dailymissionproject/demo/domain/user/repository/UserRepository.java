package dailymissionproject.demo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsername(String username);

    Optional<User> findByName(String name);

    List<User> findAll();

    List<User> findAllByName(String name);

}
