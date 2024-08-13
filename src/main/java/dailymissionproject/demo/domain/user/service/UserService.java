package dailymissionproject.demo.domain.user.service;

import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
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

    /**
     * 유저 정보 확인
     * @param username
     * @return userResDto
     */
    @Transactional(readOnly = true)
    public UserResDto detail(String username){

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        UserResDto res = UserResDto.builder()
                .name(username)
                .email(findUser.getEmail())
                .imgUrl(findUser.getImgUrl())
                .build();

        return res;
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

        String imgUrl = imageService.uploadImg(file);
        findUser.setImg(imgUrl);

        return userRepository.save(findUser).getId();
    }
}
