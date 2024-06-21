package dailymissionproject.demo.domain.participant.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParticipant is a Querydsl query type for Participant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipant extends EntityPathBase<Participant> {

    private static final long serialVersionUID = 1299623729L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParticipant participant = new QParticipant("participant");

    public final BooleanPath banned = createBoolean("banned");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final dailymissionproject.demo.domain.mission.repository.QMission mission;

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final dailymissionproject.demo.domain.user.repository.QUser user;

    public QParticipant(String variable) {
        this(Participant.class, forVariable(variable), INITS);
    }

    public QParticipant(Path<? extends Participant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipant(PathMetadata metadata, PathInits inits) {
        this(Participant.class, metadata, inits);
    }

    public QParticipant(Class<? extends Participant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mission = inits.isInitialized("mission") ? new dailymissionproject.demo.domain.mission.repository.QMission(forProperty("mission"), inits.get("mission")) : null;
        this.user = inits.isInitialized("user") ? new dailymissionproject.demo.domain.user.repository.QUser(forProperty("user")) : null;
    }

}

