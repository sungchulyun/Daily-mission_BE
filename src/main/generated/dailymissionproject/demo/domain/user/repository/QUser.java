package dailymissionproject.demo.domain.user.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1930796077L;

    public static final QUser user = new QUser("user");

    public final dailymissionproject.demo.common.repository.QBaseTimeEntity _super = new dailymissionproject.demo.common.repository.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final ListPath<dailymissionproject.demo.domain.mission.repository.Mission, dailymissionproject.demo.domain.mission.repository.QMission> missions = this.<dailymissionproject.demo.domain.mission.repository.Mission, dailymissionproject.demo.domain.mission.repository.QMission>createList("missions", dailymissionproject.demo.domain.mission.repository.Mission.class, dailymissionproject.demo.domain.mission.repository.QMission.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final ListPath<dailymissionproject.demo.domain.participant.repository.Participant, dailymissionproject.demo.domain.participant.repository.QParticipant> participants = this.<dailymissionproject.demo.domain.participant.repository.Participant, dailymissionproject.demo.domain.participant.repository.QParticipant>createList("participants", dailymissionproject.demo.domain.participant.repository.Participant.class, dailymissionproject.demo.domain.participant.repository.QParticipant.class, PathInits.DIRECT2);

    public final ListPath<dailymissionproject.demo.domain.post.repository.Post, dailymissionproject.demo.domain.post.repository.QPost> posts = this.<dailymissionproject.demo.domain.post.repository.Post, dailymissionproject.demo.domain.post.repository.QPost>createList("posts", dailymissionproject.demo.domain.post.repository.Post.class, dailymissionproject.demo.domain.post.repository.QPost.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

