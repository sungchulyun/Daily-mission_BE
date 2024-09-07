package dailymissionproject.demo.domain.auth.service;

import dailymissionproject.demo.domain.auth.dto.*;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Optional<User> findUser = userRepository.findByUsername(username);
        if(!findUser.isPresent()){

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setName(oAuth2Response.getName());
            userDto.setEmail(oAuth2Response.getEmail());
            userDto.setNickname(oAuth2Response.getNickname());
            userDto.setImageUrl(oAuth2Response.getProfileImage());
            userDto.setRole("USER");

            User user = userDto.toEntity(userDto);
            Long id = userRepository.save(user).getId();

            userDto.setId(id);
            return CustomOAuth2User.create(userDto);

        } else {
            User user = findUser.get();
            //user.setEmail(oAuth2Response.getEmail());
            //user.setImageUrl(oAuth2Response.getProfileImage());
            //user.setName(oAuth2Response.getName());
            //user.setNickname(oAuth2Response.getNickname());

            //userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setName(oAuth2User.getName());
            userDto.setEmail(oAuth2Response.getEmail());
            userDto.setNickname(oAuth2Response.getNickname());
            userDto.setImageUrl(oAuth2Response.getProfileImage());
            userDto.setRole("ROLE_USER");
            userDto.setId(user.getId());

            return CustomOAuth2User.create(userDto, oAuth2User.getAttributes());
        }

    }
}
