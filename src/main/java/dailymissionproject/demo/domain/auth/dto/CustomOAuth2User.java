package dailymissionproject.demo.domain.auth.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private UserDto userDto;

    private Long id;
    private String email;
    private String username;
    private String userNickname;
    private String imageUrl;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomOAuth2User(Long id, String email, String username, String userNickname, String imageUrl, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userNickname = userNickname;
        this.imageUrl = imageUrl;
        this.authorities = authorities;
    }

    public static CustomOAuth2User create(UserDto user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_USER";
            }
        });

        return new CustomOAuth2User(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getNickname(),
                user.getImageUrl(),
                authorities
        );
    }

    public static CustomOAuth2User create(UserDto user, Map<String, Object> attributes){
        CustomOAuth2User customOAuth2User = CustomOAuth2User.create(user);
        customOAuth2User.setAttributes(attributes);

        return customOAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                //return userDto.getRole().toString();
                return "ROLE_USER";
            }
        });
        return collection;
    }

    public Long getId(){ return id; }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    public String getUsername(){
        return this.username;
    }

    public String getNickname(){
        return userNickname;
    }

    public void setAttributes(Map<String, Object> attributes){
        this.attributes = attributes;
    }
}
