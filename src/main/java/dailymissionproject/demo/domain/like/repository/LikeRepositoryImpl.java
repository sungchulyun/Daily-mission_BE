package dailymissionproject.demo.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepositoryImpl extends LikesRepository, JpaRepository<Likes, Long> {

    @Override
    boolean exitsByPostIdAndUserId(Long postId, Long userId);
}
