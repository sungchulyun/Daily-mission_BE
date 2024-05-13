package dailymissionproject.demo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
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
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;

    private boolean ended;
    private boolean deleted;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

}
