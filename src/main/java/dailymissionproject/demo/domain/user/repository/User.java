package dailymissionproject.demo.domain.user.repository;

import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.post.repository.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Builder
    public User(String name, String email, String username, String imageUrl, Role role){
        this.name = name;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    @Builder
    public User(String name, String username, String email, String imageUrl){
        this.name = name;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
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
}
