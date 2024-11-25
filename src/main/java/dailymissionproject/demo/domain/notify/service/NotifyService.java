package dailymissionproject.demo.domain.notify.service;

import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.repository.EmitterRepositoryImpl;
import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.notify.repository.NotifyRepository;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepositoryImpl emitterRepository;
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;


    //Emitter 생성
    public SseEmitter subscribe(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
        SseEmitter emitter = create(userId);

        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(userId))
                    .data("Event created. [userId = " + userId + "]"));
        } catch (IOException e) {
            throw new RuntimeException("전송에 실패했습니다.");
        }

        return emitter;
    }

    public void send(User receiver, NotificationType notificationType, Object event){
        Notification notification = Notification.builder()
                .receiver(receiver)
                .content(String.valueOf(event))
                .notificationType(notificationType)
                .build();

        SseEmitter emitter = emitterRepository.get(receiver.getId());

        if (Objects.isNull(emitter)) {
            log.warn("연결중이지 않은 userId={}", receiver.getId());
            notifyRepository.save(notification);
            return;
        }

        NotifyDto response = NotifyDto.builder()
                .id(receiver.getId())
                .content(String.valueOf(event))
                .type(notificationType)
                .build();

        sendNotification(emitter, response);
        notifyRepository.save(notification);
    }

    private void sendNotification(SseEmitter emitter, NotifyDto response) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(response.getId()))
                    .data(response));
        } catch (IOException e) {
            throw new RuntimeException("전송에 실패했습니다.");
        }
    }

    private SseEmitter create(Long userId){
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        return emitter;
    }
}
