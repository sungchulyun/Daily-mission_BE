package dailymissionproject.demo.domain.like.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
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

@Service
@RequiredArgsConstructor
public class likesService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    //좋아요를 누를 수 있다.
    public LikesResponseDto save(LikesRequestDto likeRequest) {
        Post findPost = postRepository.findById(likeRequest.getPostId())
                .orElseThrow(() -> new PostException(PostExceptionCode.POST_NOT_FOUND));

        User findUser = userRepository.findById(likeRequest.getUserId())
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        Likes findLike = likesRepository.findByPostIdAndUserId(findPost.getId(), findUser.getId())
                .orElseThrow(() -> new LikesException(LikesExceptionCode.INVALID_LIKE_REQUEST));

        Likes likes = Likes.builder()
                .post(findPost)
                .user(findUser)
                .build();

        likesRepository.save(likes);

        return LikesResponseDto.builder()
                .postId(findPost.getId())
                .userId(findUser.getId())
                .build();
    }

    //추가한 좋아요를 취소할 수 있다.
}
