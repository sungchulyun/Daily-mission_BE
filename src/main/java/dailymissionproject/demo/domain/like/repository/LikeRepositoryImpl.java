package dailymissionproject.demo.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepositoryImpl extends LikeRepository, JpaRepository<Likes, Long> {

    @Override
    public Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);
}
