package dailymissionproject.demo.domain.notify.service;

import dailymissionproject.demo.domain.notify.repository.EmitterRepositoryImpl;
import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.notify.repository.NotifyRepository;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepositoryImpl emitterRepository;
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;


    //Emitter 생성
    public SseEmitter subscribe(Long userId){
        SseEmitter emitter = create(userId);

        sendToClient(userId, "Event stream is created. [userId : " +  userId + "]");
        return emitter;
    }

    public void notify(User receiver, NotificationType notificationType, Object event){
        Notification notification = Notification.builder()
                .receiver(receiver)
                .content(String.valueOf(event))
                .notificationType(notificationType)
                .build();

        sendToClient(receiver.getId(), event);
        notifyRepository.save(notification);
    }

    private void sendToClient(Long userId, Object data) {
        SseEmitter emitter = emitterRepository.get(userId);

        if(!Objects.isNull(emitter)){
            try {
                emitter.send(data);
            } catch (IOException e) {
                throw new RuntimeException("연결 오류!");
            }
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
