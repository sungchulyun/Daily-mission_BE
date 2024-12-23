package dailymissionproject.demo.domain.notify.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.dto.response.UserNotifyResponseDto;
import dailymissionproject.demo.domain.notify.exception.NotificationException;
import dailymissionproject.demo.domain.notify.exception.NotificationExceptionCode;
import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.notify.repository.NotificationRepository;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RedisMessageService redisMessageService;

    public void createNotification(User receiver, NotificationType notificationType, Object event){
        NotifyDto notifyDto = NotifyDto.builder()
                .id(receiver.getId())
                .content(String.valueOf(event))
                .type(notificationType)
                .build();

        redisMessageService.publish(String.valueOf(receiver.getId()), notifyDto);
    }

    @Transactional(readOnly = true)
    public PageResponseDto getUserNotifications(CustomOAuth2User user, Pageable pageable){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        Slice<UserNotifyResponseDto> responses = notificationRepository.findNotificationByUserId(findUser.getId(), pageable);

        return new PageResponseDto(responses.getContent(), responses.hasNext());
    }

    @Transactional
    public boolean readNotification(CustomOAuth2User user, Long id){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(NotificationExceptionCode.NOTIFICATION_NOT_FOUND));

        notification.confirmNotification();
        return true;
    }
}
