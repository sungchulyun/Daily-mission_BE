package dailymissionproject.demo.domain.user.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.user.dto.request.UserUpdateRequestDto;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.dto.response.UserUpdateResponseDto;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.NICKNAME_ALREADY_EXITS;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    /**
     * 유저 세부 정보 확인하는 메서드
     * @param user
     * @return UserDetailResponseDto
     */
    @Transactional(readOnly = true)
    public UserDetailResponseDto detail(CustomOAuth2User user) {

        log.info("user.id = {}", user.getId());
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return UserDetailResponseDto.builder()
                .nickname(findUser.getNickname())
                .email(findUser.getEmail())
                .imageUrl(findUser.getImageUrl())
                .build();
    }

    /**
     * 유저 정보 업데이트 메서드
     * @param
     * @param file
     * @return UserUpdateResponseDto
     */
    @Transactional
    public UserUpdateResponseDto updateProfile(CustomOAuth2User user, UserUpdateRequestDto request, MultipartFile file) throws IOException {

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Optional<User> hasNicknameUser = userRepository.findByNickname(request.getNickname());
        // 변경하려는 닉네임이 사용중이면, 에러를 반환한다.
        if(hasNicknameUser.isPresent()) throw new UserException(NICKNAME_ALREADY_EXITS);

        //수정할 프로필 이미지 존재 유무 검증
        if(file != null){
            String updatedImageUrl = imageService.uploadUserS3(file, findUser.getUsername());

            findUser.setImageUrl(updatedImageUrl);
            findUser.setNickname(request.getNickname());

            return UserUpdateResponseDto.builder()
                    .username(findUser.getUsername())
                    .nickname(findUser.getNickname())
                    .imageUrl(findUser.getImageUrl())
                    .build();
        }

        findUser.setNickname(request.getNickname());

        return UserUpdateResponseDto.builder()
                .username(findUser.getUsername())
                .nickname(findUser.getNickname())
                .build();
    }
}
