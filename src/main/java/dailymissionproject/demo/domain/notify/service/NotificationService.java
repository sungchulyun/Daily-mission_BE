package dailymissionproject.demo.domain.notify.service;

import dailymissionproject.demo.domain.auth.dto.CustomOAuth2User;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.dto.response.UserNotifyResponseDto;
import dailymissionproject.demo.domain.notify.repository.NotificationRepository;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.user.repository.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, NotifyDto> kafkaTemplate;
    private final NotificationRepository notificationRepository;

    public void createNotification(User receiver, NotificationType notificationType, Object event){
        NotifyDto notifyDto = NotifyDto.builder()
                .id(receiver.getId())
                .content(String.valueOf(event))
                .type(notificationType)
                .build();

        kafkaTemplate.send("notify", notifyDto);
    }

    @Transactional(readOnly = true)
    public PageResponseDto getUserNotifications(CustomOAuth2User user, Pageable pageable){
        Slice<UserNotifyResponseDto> responses = notificationRepository.findUnreadNotificationByUserId(user.getId(), pageable);

        return new PageResponseDto(responses.getContent(), responses.hasNext());
    }
}
