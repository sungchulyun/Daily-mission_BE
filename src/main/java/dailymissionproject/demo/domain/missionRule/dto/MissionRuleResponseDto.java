package dailymissionproject.demo.domain.missionRule.dto;

import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionRuleResponseDto {

    private Week week;

    private boolean deleted;

    public MissionRuleResponseDto(Week week, boolean deleted) {
        this.week = week;
        this.deleted = deleted;
    }

    public static MissionRuleResponseDto of(MissionRule missionRule) {
        return new MissionRuleResponseDto(missionRule.getWeek(), missionRule.isDeleted());
    }


}
