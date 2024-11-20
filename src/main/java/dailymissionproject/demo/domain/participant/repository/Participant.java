package dailymissionproject.demo.domain.participant.repository;

import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.user.repository.User;
import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Participant extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean banned;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Builder
    public Participant(User user, Mission mission){
        this.user = user;
        this.mission = mission;
        //default
        this.banned = false;
    }

    public void ban(){
        this.banned = true;
    }

    @Override
    public boolean equals(Object obj){

        Participant participant = (Participant) obj;

        if(participant != null && participant.user.equals(this.user)){
            return true;
        } else {
            return false;
        }
    }
}
