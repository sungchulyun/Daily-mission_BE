package dailymissionproject.demo.domain.mission.repository;

import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.user.repository.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Mission extends BaseTimeEntity {

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
    public Mission(User user, String title, String content, String imageUrl, String credential,
                   LocalDate startDate, LocalDate endDate){
        this.user = user;
        this.title = title;
        this.content = content;
        this.credential = credential;
        //s3
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        //default
        this.ended = false;
        this.deleted = false;
    }

    /**
     * 설명 : 미션 참여중인 유저목록 List
     *      아이디 / 이름 / 이미지/ 강퇴여부
     */
    public List<ParticipantUserDto> getAllParticipantUser(){
        List<ParticipantUserDto> participantUserList = new ArrayList<>();

        for(Participant p : this.participants){
            User user = p.getUser();

            ParticipantUserDto participantUser = ParticipantUserDto.builder()
                    .id(user.getId())
                    .userName(user.getName())
                    .imgUrl(user.getImageUrl())
                    .banned(p.isBanned())
                    .build();

            participantUserList.add(participantUser);
        }
        return participantUserList;
    }

    /*
    *미션에 참여중인 유저 중 강퇴당하지 않은 사용자들 count
     */
    public int getParticipantCountNotBanned(){
        int count = 0;
        for(Participant p : this.participants){
            if(!p.isBanned()) count++;
        }
        return count;
    }

    /*
    *미션 삭제 가능 확인
     */
    public boolean isDeletable(User user){

        if(this.user.getId() != user.getId()){
            throw new IllegalArgumentException("미션 생성자만 삭제가 가능합니다.");
        }

        if(this.deleted){
            throw new IllegalArgumentException("이미 삭제된 미션입니다.");
        }

        return true;
    }

    /*
    미션 시작 날짜 검증
     */
    public boolean isValidStartDate(LocalDate now){
        if(this.startDate.isBefore(now)){
            throw new IllegalArgumentException("미션 시작날짜는 현재보다 빠를 수 없습니다.");
        }

        if(this.startDate.isAfter(this.endDate)){
            throw new IllegalArgumentException("미션 시작날짜는 미션종료날짜보다 느릴 수 없습니다.");
        }
        return true;
    }
}
