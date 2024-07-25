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
import java.util.NoSuchElementException;
import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    public void updateProfile(String username, MultipartFile file) throws IOException {

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        String imgUrl = imageService.uploadImg(file);
        findUser.setImageUrl(imgUrl);
        userRepository.save(findUser);
    }

    @Transactional(readOnly = true)
    public UserResDto getUserInfo(String username){

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        UserResDto res = UserResDto.builder()
                .name(username)
                .email(findUser.getEmail())
                .imgUrl(findUser.getImageUrl())
                .build();

        return res;
    }
}
