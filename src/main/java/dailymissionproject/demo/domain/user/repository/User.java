package dailymissionproject.demo.domain.user.repository;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.post.repository.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "User")
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Post> posts = new ArrayList<>();

   @OneToMany(mappedBy = "user")
    List<Participant> participants = new ArrayList<>();

    private String name;
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "image_url")
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Builder
    public User(String name, String email, String username, String imgUrl, Role role){
        this.name = name;
        this.email = email;
        this.username = username;
        this.imgUrl = imgUrl;
        this.role = role;
    }

    @Builder
    public User(String name, String username, String email, String imgUrl){
        this.name = name;
        this.username = username;
        this.email = email;
        this.imgUrl = imgUrl;
    }


    public String getRoleKey(){
        return this.role.getKey();
    }

    public void setImg(String imgUrl){
        this.imgUrl = imgUrl;
    }
}
