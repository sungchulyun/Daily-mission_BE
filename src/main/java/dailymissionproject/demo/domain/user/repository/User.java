package dailymissionproject.demo.domain.user.repository;

import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.post.repository.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "User")
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Notification> notifications = new ArrayList<>();

    private String name;
    private String email;
    private String nickname;

    @Column(name = "username")
    private String username;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Builder
    public User(String name, String email, String username, String nickname, String imageUrl, Role role){
        this.name = name;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    @Builder
    public User(String name, String username, String nickname, String email, String imageUrl){
        this.name = name;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    @Builder
    public User(Long id, String name, String email, String nickname){
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }


    public String getRoleKey(){
        return this.role.getKey();
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof User user))
            return false;

        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "email is: " + this.getEmail()+
                "\n" + "name is" + this.getName() +
                "\n" + "nickname is:" + this.getNickname();
    }
}
