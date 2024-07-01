package dailymissionproject.demo.domain.auth.service;

import dailymissionproject.demo.domain.auth.dto.*;
import dailymissionproject.demo.domain.auth.repository.AuthRepository;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthRepository authRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

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
        User findUser = authRepository.findByUsername(username);

        if(Objects.isNull(findUser)){

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setName(oAuth2Response.getName());
            userDto.setEmail(oAuth2Response.getEmail());
            userDto.setImageUrl(oAuth2Response.getProfileImage());
            userDto.setRole(Role.USER);

            authRepository.save(findUser);

            return new CustomOAuth2User(userDto);

        } else {
            findUser.setEmail(oAuth2Response.getEmail());
            findUser.setImageUrl(oAuth2Response.getProfileImage());
            findUser.setName(oAuth2Response.getName());

            authRepository.save(findUser);

            UserDto userDto = new UserDto();
            userDto.setUsername(findUser.getUsername());
            userDto.setName(oAuth2User.getName());
            userDto.setEmail(oAuth2Response.getEmail());
            userDto.setImageUrl(oAuth2Response.getProfileImage());
            userDto.setRole(findUser.getRole());

            return new CustomOAuth2User(userDto);
        }

    }
}
