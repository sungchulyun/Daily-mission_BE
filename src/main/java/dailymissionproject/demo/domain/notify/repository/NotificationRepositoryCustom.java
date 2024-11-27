package dailymissionproject.demo.domain.notify.repository;

import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.dto.response.UserNotifyResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationRepositoryCustom {

    Slice<UserNotifyResponseDto> findNotificationByUserId(Long userId, Pageable pageable);
}
