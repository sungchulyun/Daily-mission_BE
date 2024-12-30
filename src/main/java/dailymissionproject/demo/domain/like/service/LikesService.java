package dailymissionproject.demo.domain.like.service;

import dailymissionproject.demo.domain.like.dto.response.LikesResponseDto;
import dailymissionproject.demo.domain.like.repository.Likes;
import dailymissionproject.demo.domain.like.repository.LikesRepository;
import dailymissionproject.demo.domain.post.exception.PostException;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static dailymissionproject.demo.domain.post.exception.PostExceptionCode.POST_NOT_FOUND;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    /**
     * 좋아요를 누른 히스토리가 있을 경우 -> deleteLike 수행
     * 좋아요 히스토리가 없을 경우 -> addLike 수행
     * @param postId
     * @param userId
     * @return
     */
    @Transactional
    public LikesResponseDto likePost(Long postId, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Post findPost = postRepository.findWithPessimisticLockById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        boolean isLiked = likesRepository.findByPostIdAndUserId(postId, userId)
                .map(likes -> {
                    removeLike(findPost, likes);
                    return false;
                })
                .orElseGet(() -> {
                    addLike(findPost, findUser);
                    return true;
                });

        return LikesResponseDto.builder()
                .postId(postId)
                .userId(userId)
                .isLiked(isLiked)
                .build();
    }

    private void addLike(Post post, User user) {
        Likes likes = Likes.builder()
                .post(post)
                .user(user)
                .build();

        post.incrementLikeCount();

        likesRepository.save(likes);
    }

    private void removeLike(Post post, Likes likes) {
            post.decrementLikeCount();

            likesRepository.delete(likes);
    }
}
