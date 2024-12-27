package dailymissionproject.demo.domain.post.repository;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.like.repository.Likes;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.user.repository.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Likes> likes = new ArrayList<>();

    private String title;
    private String content;
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "like_count")
    private Long likeCount;

    private boolean deleted;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Builder
    public Post(Mission mission, User user, String title, String content, String imageUrl){
        this.mission = mission;
        this.user = user;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likeCount = 0L;
    }

    public void update(String title, String content, String imgUrl){
        this.title = title;
        this.content = content;
        this.imageUrl = imgUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title){this.title = title;}

    public void setContent(String content){this.content = content;}

    public void incrementLikeCount(){this.likeCount++;}

    public void decrementLikeCount(){
        if(this.likeCount > 0){
            this.likeCount--;
        }
    }

    public void delete(){this.deleted = true;}
}
