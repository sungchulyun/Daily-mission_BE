package dailymissionproject.demo.domain.missionRule.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@Schema(description = "미션 필수 제출 요일")
public class Week {

    @Column(name = "SUNDAY", nullable = false)
    private boolean sun;

    @Column(name = "MONDAY", nullable = false)
    private boolean mon;

    @Column(name = "TUESDAY", nullable = false)
    private boolean tue;

    @Column(name = "WEDNESDAY", nullable = false)
    private boolean wed;

    @Column(name = "THURSDAY", nullable = false)
    private boolean thu;

    @Column(name = "FRIDAY", nullable = false)
    private boolean fri;

    @Column(name = "SATURDAY", nullable = false)
    private boolean sat;

    @Builder
    public Week(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }

    public void update(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }
}
