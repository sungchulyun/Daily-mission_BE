package dailymissionproject.demo.domain.post.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{

      Post save(Post post);

      @Lock(LockModeType.PESSIMISTIC_WRITE)
      @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
      @Query("SELECT p FROM Post p WHERE p.id = :id")
      Optional<Post> findWithPessimisticLockById(Long id);
}
