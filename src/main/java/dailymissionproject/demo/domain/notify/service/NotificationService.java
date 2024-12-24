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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static dailymissionproject.demo.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmitterService emitterService;
    private final RedisMessageService redisMessageService;

    public SseEmitter subscribe(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        SseEmitter emitter = emitterService.create(userId);
        emitter.onTimeout(emitter::complete);
        emitter.onError((e) -> emitter.complete());
        emitter.onCompletion(() -> {
            emitterService.deleteEmitter(userId);
            redisMessageService.removeSubscribe(String.valueOf(userId));
        });

        // 최초 connection 생성
        emitterService.subscribe(emitter, userId);
        redisMessageService.subscribe(String.valueOf(userId));

        return emitter;
    }

    @Transactional
    public void sendNotification(NotifyDto request){
        User receiver = userRepository.findById(request.getId()).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Notification notification = Notification.builder()
                .receiver(receiver)
                .content(request.getContent())
                .notificationType(request.getType())
                .build();

        notificationRepository.save(notification);
        redisMessageService.publish(String.valueOf(request.getId()), request);
    }

    @Transactional(readOnly = true)
    public PageResponseDto getUserNotifications(CustomOAuth2User user, Pageable pageable){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Slice<UserNotifyResponseDto> responses = notificationRepository.findNotificationByUserId(findUser.getId(), pageable);

        return new PageResponseDto(responses.getContent(), responses.hasNext());
    }

    @Transactional
    public boolean readNotification(CustomOAuth2User user, Long id){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(NotificationExceptionCode.NOTIFICATION_NOT_FOUND));

        notification.confirmNotification();
        return true;
    }
}
