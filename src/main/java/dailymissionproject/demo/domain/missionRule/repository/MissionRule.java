package dailymissionproject.demo.domain.missionRule.repository;

import dailymissionproject.demo.domain.mission.repository.Mission;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MissionRule {

    @Id @GeneratedValue
    @Column(name = "mission_rule_id")
    private Long id;

    @Embedded
    private Week week;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Builder
    public MissionRule(Week week){
        if(week==null){
            throw new IllegalArgumentException("week 값은 필수사항 입니다.");
        }

        this.week = week;
        this.deleted = false;
    }

    @OneToOne(mappedBy = "missionRule")
    private Mission mission;

    public void update(Week week){

        if(week==null){
            throw new IllegalArgumentException("week 값은 필수사항 입니다.");
        }

        this.week = week;
    }

    public void delete(){
        this.deleted = true;
    }

    //제출요일 검증
    public boolean isValidWeek(){

        // true if all week == false
        if(!(this.week.isSun()
                || this.week.isMon()
                || this.week.isTue()
                || this.week.isWed()
                || this.week.isThu()
                || this.week.isFri()
                || this.week.isSat())){
            throw new IllegalArgumentException("최소 하루의 제출요일을 선택해야 합니다.");
        }

        return true;
    }
}