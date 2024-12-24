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
    private final UserRepository userRepository;

    private final static String NOTIFY_NAME = "notify";
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(SseEmitter emitter, Long userId){
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(userId))
                    .name(NOTIFY_NAME)
                    .data("Event created. [userId : " + userId + "]"));
        } catch (IOException e) {
            throw new RuntimeException("전송에 실패했습니다.");
        }

        return emitter;
    }

    public void send(NotifyDto request){
        User user = userRepository.findById(request.getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        SseEmitter emitter = emitterRepository.get(request.getId());
        if(emitter != null){
            sendNotificationToClient(emitter, request);
        }
    }

    private void sendNotificationToClient(SseEmitter emitter, NotifyDto response) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(response.getId()))
                    .data(response));
        } catch (IOException e) {
            log.error("IOException | IllegalException is Occurred.");
            emitterRepository.delete(response.getId());
        }
    }

    public SseEmitter create(Long userId){
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        return emitter;
    }

    public void deleteEmitter(Long userId){
        emitterRepository.delete(userId);
    }
}
