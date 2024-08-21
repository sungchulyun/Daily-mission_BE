package dailymissionproject.demo.domain.missionRule.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeek is a Querydsl query type for Week
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QWeek extends BeanPath<Week> {

    private static final long serialVersionUID = 1056190251L;

    public static final QWeek week = new QWeek("week");

    public final BooleanPath fri = createBoolean("fri");

    public final BooleanPath mon = createBoolean("mon");

    public final BooleanPath sat = createBoolean("sat");

    public final BooleanPath sun = createBoolean("sun");

    public final BooleanPath thu = createBoolean("thu");

    public final BooleanPath tue = createBoolean("tue");

    public final BooleanPath wed = createBoolean("wed");

    public QWeek(String variable) {
        super(Week.class, forVariable(variable));
    }

    public QWeek(Path<? extends Week> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeek(PathMetadata metadata) {
        super(Week.class, metadata);
    }

}

