package dailymissionproject.demo.domain.notify.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dailymissionproject.demo.domain.notify.dto.response.UserNotifyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static dailymissionproject.demo.domain.notify.repository.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<UserNotifyResponseDto> findUnreadNotificationByUserId(Long userId, Pageable pageable) {
        List<UserNotifyResponseDto> userNotifies = fetchUnreadNotificationByUserId(userId, pageable);
        boolean hasNext = hasNextPage(userNotifies, pageable);

        return new SliceImpl<>(userNotifies, pageable, hasNext);
    }

    public List<UserNotifyResponseDto> fetchUnreadNotificationByUserId(Long userId, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(UserNotifyResponseDto.class,
                        notification.notificationType,
                        notification.content))
                .from(notification)
                .where(notification.receiver.id.eq(userId).and(notification.checked.isFalse()))
                .orderBy(notification.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private boolean hasNextPage(List<?> notifications, Pageable pageable) {
        if(notifications.size() > pageable.getPageSize()) {
            notifications.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }
}
