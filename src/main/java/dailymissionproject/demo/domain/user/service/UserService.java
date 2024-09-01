package dailymissionproject.demo.domain.user.service;

import dailymissionproject.demo.common.util.S3Util;
import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.user.dto.response.UserDetailResponseDto;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final S3Util s3Util;

    /**
     * 유저 정보 확인
     * @param user
     * @return userResDto
     */
    @Transactional(readOnly = true)
    public UserDetailResponseDto detail(CustomOAuth2User user) {

        User findUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return UserDetailResponseDto.builder()
                .userNickname(findUser.getNickname())
                .email(findUser.getEmail())
                .imageUrl(findUser.getImageUrl())
                .build();
    }

    /**
     * 유저 정보 업데이트
     * @param username
     * @param file
     * @return user pk Id
     */
    @Transactional
    public Long updateProfile(String username, MultipartFile file) throws IOException {

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        String imageUrl = imageService.uploadUserS3(file, username);
        findUser.setImageUrl(imageUrl);

        return userRepository.save(findUser).getId();
    }
}
