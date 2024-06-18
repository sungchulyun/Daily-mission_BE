package dailymissionproject.demo.domain.mission.repository;

import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Mission {

    @Id @GeneratedValue
    @Column(name = "mission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    private String title;
    private String content;
    @Column(name = "image_url")
    private String imageUrl;
    private String credential;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean ended;
    private boolean deleted;


    //== 생성 메서드 ==//
    @Builder
    public Mission(User user, String title, String content, String imageUrl,
                   LocalDate startDate, LocalDate endDate){
        this.user = user;
        this.title = title;
        this.content = content;

        //s3
        this.imageUrl = imageUrl;

        this.ended = false;
        this.deleted = false;
    }
}
