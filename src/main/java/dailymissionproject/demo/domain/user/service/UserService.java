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
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    public UserResDto join(UserReqDto userReqDto){
        User user = userReqDto.toEntity(userReqDto);
        validateName(user);
        userRepository.save(user);

       UserResDto res = UserResDto.builder()
                .name(userReqDto.getName())
                .code(200)
                .msgCode("성공적으로 회원 가입 완료.")
               .build();
       return res;
    }

    //이메일 중복 검증 로직 oauth2 도입 시 수정 필요
    private void validateName(User user) {
        List<User> findUser = userRepository.findByMail(user.getEmail());
        if(!findUser.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional
    public void updateProfile(String userName, MultipartFile file) throws IOException {
        User findUser = userRepository.findOneByName(userName);
        isUserExists(userName);

        String imgUrl = imageService.uploadImg(file);
        findUser.setImageUrl(imgUrl);
        userRepository.save(findUser);
    }

    public void isUserExists(String name){
        List<User> findUser = userRepository.findByName(name);
        if(Objects.isNull(findUser)){
            throw new RuntimeException("없는 사용자 이름입니다.");
        }
    }

    private boolean validateUpdateName(String name){
       if(name == null){
           return false;
       }
       List<User> findUser = userRepository.findByName(name);
       if(!findUser.isEmpty()){
           return false;
       }
       return true;
    }



    @Transactional(readOnly = true)
    public List<User> findUser(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findOne(Long id){
        return userRepository.findOne(id);
    }

}
