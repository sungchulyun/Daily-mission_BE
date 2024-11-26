package dailymissionproject.demo.domain.notify.service;

import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final KafkaTemplate<String, NotifyDto> kafkaTemplate;

    public void createNotification(User receiver, NotificationType notificationType, Object event){
        NotifyDto notifyDto = NotifyDto.builder()
                .id(receiver.getId())
                .content(String.valueOf(event))
                .type(notificationType)
                .build();

        kafkaTemplate.send("notify", notifyDto);
    }
}
