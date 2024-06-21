package dailymissionproject.demo.domain.mission.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMission is a Querydsl query type for Mission
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMission extends EntityPathBase<Mission> {

    private static final long serialVersionUID = 789336337L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMission mission = new QMission("mission");

    public final dailymissionproject.demo.common.repository.QBaseTimeEntity _super = new dailymissionproject.demo.common.repository.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath credential = createString("credential");

    public final BooleanPath deleted = createBoolean("deleted");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final BooleanPath ended = createBoolean("ended");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final ListPath<dailymissionproject.demo.domain.participant.repository.Participant, dailymissionproject.demo.domain.participant.repository.QParticipant> participants = this.<dailymissionproject.demo.domain.participant.repository.Participant, dailymissionproject.demo.domain.participant.repository.QParticipant>createList("participants", dailymissionproject.demo.domain.participant.repository.Participant.class, dailymissionproject.demo.domain.participant.repository.QParticipant.class, PathInits.DIRECT2);

    public final ListPath<dailymissionproject.demo.domain.post.repository.Post, dailymissionproject.demo.domain.post.repository.QPost> posts = this.<dailymissionproject.demo.domain.post.repository.Post, dailymissionproject.demo.domain.post.repository.QPost>createList("posts", dailymissionproject.demo.domain.post.repository.Post.class, dailymissionproject.demo.domain.post.repository.QPost.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    public final dailymissionproject.demo.domain.user.repository.QUser user;

    public QMission(String variable) {
        this(Mission.class, forVariable(variable), INITS);
    }

    public QMission(Path<? extends Mission> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMission(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMission(PathMetadata metadata, PathInits inits) {
        this(Mission.class, metadata, inits);
    }

    public QMission(Class<? extends Mission> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new dailymissionproject.demo.domain.user.repository.QUser(forProperty("user")) : null;
    }

}

