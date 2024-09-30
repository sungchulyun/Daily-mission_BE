package dailymissionproject.demo.domain.mission.repository;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.mission.exception.MissionException;
import dailymissionproject.demo.domain.missionRule.dto.DateDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
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

import static dailymissionproject.demo.domain.mission.exception.MissionExceptionCode.*;

@Entity
@NoArgsConstructor
@Getter
public class Mission extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "mission_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_rule_id")
    private MissionRule missionRule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "mission")
    private List<Post> posts = new ArrayList<>();

    private String title;
    private String content;
    @Column(name = "image_url")
    private String imageUrl;
    private String hint;
    private String credential;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean ended;
    private boolean deleted;


    @Builder
    public Mission(User user, MissionRule missionRule, String title, String content, String imageUrl, String hint, String credential,
                   LocalDate startDate, LocalDate endDate){
        this.user = user;
        this.missionRule = missionRule;
        this.title = title;
        this.content = content;
        this.hint = hint;
        this.credential = credential;
        //s3
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        //default
        this.ended = false;
        this.deleted = false;
    }

    public void setCredential(String credential){
        this.credential = credential;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setHint(String hint){ this.hint = hint; }

    public void setUser(User user){this.user = user;}

    /**
     * 설명 : 참여 가능한 미션인지 검증
     *        1. 종료되지 않은 미션
     *        2. 삭제되지 않은 미션
     *        3. 시작하지 않은 미션
     * */
    public boolean isPossibleToParticipate(LocalDate now){

        // check is ended
        if(this.ended){
            return false;
        }

        // check is deleted
        if(this.deleted){
            return false;
        }

        // check is started
        if(now.isAfter(this.startDate)){
            return false;
        }

        return true;
    }

    /**
     * 미션 삭제할 때 사용하는 메서드
     */
    public void delete(){

        this.deleted = true;
        this.ended = true;

    }

    /**
     * 미션 종료할 때 사용하는 메서드
     */
    public void end(){
        this.ended = true;
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
                    .nickname(user.getNickname())
                    .imageUrl(user.getImageUrl())
                    .banned(p.isBanned())
                    .build();

            participantUserList.add(participantUser);
        }
        return participantUserList;
    }

    /**
     * 미션 수행중 강퇴당하지 않은 참여자 수를 구하는 메서드
     * @return count
     */
    public int getParticipantCountNotBanned(){
        int count = 0;
        for(Participant p : this.participants){
            if(!p.isBanned()) count++;
        }
        return count;
    }

    /**
     * 미션 삭제할 수 있는지 검증하는 메서드
     * @param user
     * @return
     */
    public boolean isDeletable(User user){
        if(this.deleted)
            throw new MissionException(MISSION_ALREADY_DELETED);

        if(getParticipantCountNotBanned() > 1)
            throw new MissionException(INVALID_DELETE_REQUEST);

        return true;
    }


    /**
     * 미션 시작날짜를 검증하는 메서드
     * 설명 : 미션 시작일자는 미션 생성하는 시점의 날짜보다 빠를 수 없음
     * ex) 9월 10일에 미션을 생성하는데, 시작 날짜가 9월 9일일 수 없음
     * @param now
     * @return
     */
    public boolean isValidStartDate(LocalDate now){
        if(this.startDate.isBefore(now)){
            throw new MissionException(INPUT_START_VALUE_IS_NOT_VALID);
        }

        if(this.startDate.isAfter(this.endDate)){
            throw new MissionException(INPUT_DELETE_VALUE_IS_NOT_VALID);
        }
        return true;
    }

    public boolean isEndAble(LocalDate now){

        /**
         * 설명 : 종료날짜가 지난 미션을 종료
         * */
        if(now.isAfter(this.endDate)){
            return true;
        }

        /**
         * 설명 : 모두 강퇴시 참여자가 0명인 미션을 종료
         * */
        if(getParticipantCountNotBanned()==0){
            return true;
        }

        return false;
    }

    /**
     * 설명 : week 주의 일주일간 날짜 + 제출의무 요일인지 반환
     *  ex) 메서드 호출 일자 : 2024-07-02 /
     *   -> 2024-06-30 ~ 2024-07-06
     *   -> true/true/true/true/true/false/false
     * @param startDate
     * @return
     */
    public List<DateDto> getWeekDates(LocalDate startDate){

        // result list
        List<DateDto> weekDates = new ArrayList<>();

        /**
         * 설명 : 일 ~ 월 요일의 날짜  + 제출 의무 요일인지 검증
         *       ## 입력값으로 받은 now 는 항상 일요일이다.
         *  */
        for(int i=0; i<7; i++){
            LocalDate date = startDate.plusDays(i);
            String day = date.getDayOfWeek().toString();
            boolean mandatory = checkMandatory(day);

            DateDto dateDto = DateDto.builder()
                    .date(date)
                    .day(day)
                    .mandatory(mandatory)
                    .build();

            weekDates.add(dateDto);
        }

        return weekDates;
    }

    /**
     * 설명 : String 값인 요일(SUM/MON/TUE...)이 input 으로 주어졌을 때
     *        해당 요일이 제출 의무 요일인지 검증
     *  */
    public boolean checkMandatory(String day){

        Week week = this.missionRule.getWeek();

        if(day.equals("SUNDAY")){
            return week.isSun();
        }else if(day.equals("MONDAY")){
            return week.isMon();
        }else if(day.equals("TUESDAY")){
            return week.isTue();
        }else if(day.equals("WEDNESDAY")){
            return week.isWed();
        }else if(day.equals("THURSDAY")){
            return week.isThu();
        }else if(day.equals("FRIDAY")){
            return week.isFri();
        }else if(day.equals("SATURDAY")){
            return week.isSat();
        }

        return false;
    }
}
