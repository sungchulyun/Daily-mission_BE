package dailymissionproject.demo.domain.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.post.dto.PostSubmitDto;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static dailymissionproject.demo.domain.post.repository.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    //==전체 포스트 목록 조회==//
    @Override
    public List<Post> findAll() {
        return queryFactory
                .select(post)
                .from(post)
                .where(post.deleted.isFalse())
                .orderBy(post.createdDate.desc())
                .fetch();
    }

    //==미션별 포스트 목록 조회==//
    @Override
    public List<Post> findAllByUser(User user) {

        return queryFactory
                .select(post)
                .from(post)
                .where(post.user.eq(user).and(post.deleted).isFalse())
                .orderBy(post.createdDate.desc())
                .fetch();
    }

    //==유저별 포스트 목록 조회==//
    @Override
    public List<Post> findAllByMission(Mission mission) {
        return queryFactory
                .select(post)
                .from(post)
                .where(post.mission.eq(mission).and(post.deleted.isFalse()))
                .orderBy(post.createdDate.desc())
                .fetch();
    }

    //==미션별 Weekly 포스트 제출 이력을 postSubmitDto 객체로 전달받는다==//
    //새벽 3시 이전 제출은 전날 제출로 변환
    @Override
    public List<PostSubmitDto> findWeeklyPostSubmitByMission(Long id, LocalDate startDate) {

        LocalDate endDate = startDate.plusDays(7);

        LocalDateTime startDateTime = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 3,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 3, 0, 0);

        return queryFactory
                .select(Projections.constructor(PostSubmitDto.class,
                        post.createdDate.as("date"),
                        post.user.id.as("userId"),
                        post.user.name.as("username"),
                        post.user.imageUrl.as("imageUrl")
                        ))
                .from(post)
                .where(post.mission.id.eq(id).and(post.createdDate.after(startDateTime).and(post.createdDate.before(endDateTime))))
                .orderBy(post.createdDate.desc())
                .fetch();
    }


    //==금일 해당 미션에 제출 이력이 있는지 확인//
    //삭제된 포스트는 제출하지 않은 것으로 간주
    @Override
    public Long countPostSubmit(Mission mission, User user, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .select(post.countDistinct())
                .from(post)
                .where(post.mission.eq(mission)
                        .and(post.user.eq(user))
                        .and(post.createdDate.after(startDate))
                        .and(post.createdDate.before(endDate))
                        .and(post.deleted.isFalse()))
                .fetchOne();
    }
}
