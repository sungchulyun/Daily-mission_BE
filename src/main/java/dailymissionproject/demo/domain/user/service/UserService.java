package dailymissionproject.demo.domain.user.service;

import dailymissionproject.demo.domain.user.dto.request.UserReqDto;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long join(User user){
        validate(user);
        userRepository.save(user);
        return user.getId();
    }

    //이메일 중복 검증 로직 oauth2 도입 시 수정 필요
    private void validate(User user) {
        List<User> findUser = userRepository.findByMail(user.getEmail());
        if(!findUser.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<User> findUser(){
        return userRepository.findAll();
    }

    public User findOne(Long id){
        return userRepository.findOne(id);
    }

}
