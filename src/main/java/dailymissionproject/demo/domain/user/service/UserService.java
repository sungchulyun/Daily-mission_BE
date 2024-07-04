package dailymissionproject.demo.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import dailymissionproject.demo.domain.image.ImageService;
import dailymissionproject.demo.domain.user.dto.request.UserReqDto;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;


    //이메일 중복 검증 로직 oauth2 도입 시 수정 필요 -> 수정 완
    /*
    * 2024-07-04
     */

    @Transactional
    public void updateProfile(String username, MultipartFile file) throws IOException {

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        String imgUrl = imageService.uploadImg(file);
        findUser.setImageUrl(imgUrl);
        userRepository.save(findUser);
    }

    @Transactional(readOnly = true)
    public UserResDto getUserInfo(String username){

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        UserResDto res = UserResDto.builder()
                .name(username)
                .email(findUser.getEmail())
                .code(200)
                .msgCode("Success")
                .build();

        return res;
    }
}
