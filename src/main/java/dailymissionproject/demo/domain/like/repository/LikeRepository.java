package dailymissionproject.demo.domain.like.repository;

import java.util.Optional;

public interface LikeRepository {

    Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);
}
