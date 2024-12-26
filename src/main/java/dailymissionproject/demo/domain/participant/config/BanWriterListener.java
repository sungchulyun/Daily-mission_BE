package dailymissionproject.demo.domain.participant.config;

import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import dailymissionproject.demo.domain.notify.service.NotificationService;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.user.exception.UserException;
import dailymissionproject.demo.domain.user.exception.UserExceptionCode;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BanWriterListener implements ItemWriteListener<List<Participant>> {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public void afterWrite(Chunk<? extends List<Participant>> items){
        for(List<Participant> participants : items){
            participants.forEach(this::sendBanNotification);
        }
    }

    private void sendBanNotification(Participant participant) {
        User findUser = userRepository.findById(participant.getUser().getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        if(participant.isBanned()) {
            String notificationContent = "인증글 미제출로 " + participant.getMission().getTitle() + "미션에서 강제퇴장되었습니다.";

            notificationService.sendNotification(
                    NotifyDto.builder()
                            .id(participant.getUser().getId())
                            .content(notificationContent)
                            .type(NotificationType.BAN)
                            .build());
        }
    }
}
