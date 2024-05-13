package dailymissionproject.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "user_id")
    private long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Participant> participants = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "image_url")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    @Builder
    public User(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }
}
