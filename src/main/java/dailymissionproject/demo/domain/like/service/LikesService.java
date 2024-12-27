package dailymissionproject.demo.domain.like.service;

import dailymissionproject.demo.domain.like.dto.request.LikesRequestDto;
import dailymissionproject.demo.domain.like.dto.response.LikesResponseDto;
import dailymissionproject.demo.domain.like.exception.LikesException;
import dailymissionproject.demo.domain.like.exception.LikesExceptionCode;
import dailymissionproject.demo.domain.like.repository.Likes;
import dailymissionproject.demo.domain.like.repository.LikesRepository;
import dailymissionproject.demo.domain.post.exception.PostException;
import dailymissionproject.demo.domain.post.exception.PostExceptionCode;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static dailymissionproject.demo.domain.like.exception.LikesExceptionCode.INVALID_LIKE_REQUEST;
import static dailymissionproject.demo.domain.post.exception.PostExceptionCode.POST_NOT_FOUND;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public LikesResponseDto addLike(Long postId, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        boolean alreadyLiked = likesRepository.exitsByPostIdAndUserId(postId, userId);
        if(alreadyLiked) {
            throw new LikesException(INVALID_LIKE_REQUEST);
        }

        Likes likes = Likes.builder()
                .post(findPost)
                .user(findUser)
                .build();

        findPost.incrementLikeCount();
        likesRepository.save(likes);

        return LikesResponseDto.builder()
                .likeId(likes.getId())
                .userId(findUser.getId())
                .postId(findPost.getId())
                .build();
    }

    @Transactional
    public boolean removeLike(Long postId, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Optional<Likes> likes = likesRepository.findByPostIdAndUserId(postId, userId);
        if(likes.isPresent()) {
            likesRepository.delete(likes.get());
            findPost.decrementLikeCount();
            return true;
        }

        return false;
    }
}
