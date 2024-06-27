package dailymissionproject.demo.domain.missionRule.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissionRule is a Querydsl query type for MissionRule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissionRule extends EntityPathBase<MissionRule> {

    private static final long serialVersionUID = 1638341521L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissionRule missionRule = new QMissionRule("missionRule");

    public final BooleanPath deleted = createBoolean("deleted");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final dailymissionproject.demo.domain.mission.repository.QMission mission;

    public final QWeek week;

    public QMissionRule(String variable) {
        this(MissionRule.class, forVariable(variable), INITS);
    }

    public QMissionRule(Path<? extends MissionRule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissionRule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissionRule(PathMetadata metadata, PathInits inits) {
        this(MissionRule.class, metadata, inits);
    }

    public QMissionRule(Class<? extends MissionRule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mission = inits.isInitialized("mission") ? new dailymissionproject.demo.domain.mission.repository.QMission(forProperty("mission"), inits.get("mission")) : null;
        this.week = inits.isInitialized("week") ? new QWeek(forProperty("week")) : null;
    }

}

