package dailymissionproject.demo.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);

}
