package dailymissionproject.demo.domain.user.service;

import dailymissionproject.demo.common.util.S3Util;
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
     * 유저 정보 업데이트
     * @param
     * @param file
     * @return user pk Id
     */
    @Transactional
    public UserUpdateResponseDto updateProfile(CustomOAuth2User user, UserUpdateRequestDto request, MultipartFile file) throws IOException {

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if(file.isEmpty()){
            findUser.setNickname(request.getNickname());

            userRepository.save(findUser);

            return UserUpdateResponseDto.builder()
                    .username(findUser.getUsername())
                    .nickname(findUser.getNickname())
                    .build();
        }

        String imageUrl = imageService.uploadUserS3(file, findUser.getUsername());

        findUser.setImageUrl(imageUrl);
        findUser.setNickname(request.getNickname());

        userRepository.save(findUser);

        return UserUpdateResponseDto.builder()
                .username(findUser.getUsername())
                .nickname(findUser.getNickname())
                .imageUrl(findUser.getImageUrl())
                .build();
    }
}
