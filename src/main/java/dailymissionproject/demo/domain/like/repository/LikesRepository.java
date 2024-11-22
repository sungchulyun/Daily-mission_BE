package dailymissionproject.demo.domain.like.repository;

import java.util.Optional;

public interface LikesRepository {

    Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);
    Long save(Likes likes);
}
