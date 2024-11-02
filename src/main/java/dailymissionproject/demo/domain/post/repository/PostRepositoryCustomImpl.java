package dailymissionproject.demo.domain.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.post.dto.PostSubmitDto;
import dailymissionproject.demo.domain.post.dto.response.PostMissionListResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUserListResponseDto;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static dailymissionproject.demo.domain.post.repository.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    /**
     * 삭제되지 않은 포스트 전체를 리턴하는 메서드
     * @return List<Post>
     */
    @Override
    public List<Post> findAll() {
        return queryFactory
                .select(post)
                .from(post)
                .where(post.deleted.isFalse())
                .orderBy(post.createdDate.desc())
                .fetch();
    }

    /**
     * 유저별 포스트 리스트를 Slice객체로 변환하는 메서드
     * @param pageable
     * @param user
     * @return Slice<PostUserListResponseDto>
     */
    @Override
    public Slice<PostUserListResponseDto> findAllByUser(Pageable pageable, User user) {
        List<PostUserListResponseDto> postList = fetchAllByUser(pageable, user);
        boolean hasNext = hasNextPage(postList, pageable);

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    /**
     * 유저가 작성한 포스트 전체를 반환할 때 사용하는 메서드
     * @param pageable
     * @param user
     * @return
     */
    public List<PostUserListResponseDto> fetchAllByUser(Pageable pageable, User user) {
        return queryFactory.select(Projections.constructor(PostUserListResponseDto.class,
                post.id,
                post.mission.id,
                post.mission.title,
                post.title,
                post.content,
                post.imageUrl,
                post.createdTime,
                post.modifiedDate))
                .from(post)
                .where(post.user.eq(user).and(post.deleted.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getOffset() + 1)
                .orderBy(post.modifiedDate.desc())
                .fetch();
    }

    /**
     * Slice객체로 변환하기 전에 다음페이지 존재여부를 검증하는 메서드
     * @param postList
     * @param pageable
     * @return boolean
     */
    private boolean hasNextPage(List<?> postList, Pageable pageable) {
        if(postList.size() > pageable.getPageSize()){
            postList.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }

    /**
     * 미션별 포스트 리스트를 Slice 객체로 변환하는 메서드
     * @param pageable
     * @param mission
     * @return
     */
    @Override
    public Slice<PostMissionListResponseDto> findAllByMission(Pageable pageable, Mission mission) {
        List<PostMissionListResponseDto> postList = fetchAllByMission(pageable, mission);
        boolean hasNext = hasNextPage(postList, pageable);

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    /**
     * 미션별 포스트 리스트를 반환할 때 사용하는 메서드
     * @param pageable
     * @param mission
     * @return
     */
    List<PostMissionListResponseDto> fetchAllByMission(Pageable pageable, Mission mission) {
        return queryFactory
                .select(Projections.constructor(PostMissionListResponseDto.class,
                        post.id,
                        post.mission.id,
                        post.mission.title,
                        post.mission.user.nickname,
                        post.user.imageUrl,
                        post.title,
                        post.content,
                        post.imageUrl,
                        post.createdDate,
                        post.modifiedDate))
                .from(post)
                .where(post.mission.eq(mission).and(post.deleted.isFalse()))
                .orderBy(post.modifiedDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
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
                        post.user.nickname.as("nickname"),
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
