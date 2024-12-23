package dailymissionproject.demo.domain.notify.service;

import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.repository.emitter.EmitterRepositoryImpl;
import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.notify.repository.NotificationRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterService {

    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private final static String NOTIFY_NAME = "notify";
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final RedisMessageService redisMessageService;

    public SseEmitter subscribe(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
        SseEmitter emitter = create(userId);

        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(userId))
                    .name(NOTIFY_NAME)
                    .data("Event created. [userId : " + userId + "]"));
            redisMessageService.subscribe(String.valueOf(userId));
        } catch (IOException e) {
            throw new RuntimeException("전송에 실패했습니다.");
        }

        return emitter;
    }

    @Transactional
    public void send(NotifyDto request){
        User user = userRepository.findById(request.getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        SseEmitter emitter = emitterRepository.get(request.getId());

        Notification notification = Notification.builder()
                .receiver(user)
                .content(request.getContent())
                .notificationType(request.getType())
                .build();

        if (Objects.isNull(emitter)) {
            log.warn("구독중이지 않은 User : {}", request.getId());
            notificationRepository.save(notification);
            return;
        }

        sendNotification(emitter, request);
        notificationRepository.save(notification);
    }

    private void sendNotification(SseEmitter emitter, NotifyDto response) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(response.getId()))
                    .data(response));
        } catch (IOException e) {
            log.error("IOException | IllegalException is Occurred.");
            emitterRepository.delete(response.getId());
        }
    }

    private SseEmitter create(Long userId){
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        redisMessageService.subscribe(String.valueOf(userId));
        emitter.onCompletion(() -> disconnect(userId));
        emitter.onTimeout(() -> disconnect(userId));

        return emitter;
    }

    private void disconnect(Long userId){
        emitterRepository.delete(userId);
        redisMessageService.removeSubscribe(String.valueOf(userId));
    }
}
