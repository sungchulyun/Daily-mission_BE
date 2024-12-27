package dailymissionproject.demo.domain.mission.batch;

import dailymissionproject.demo.domain.mission.repository.Mission;
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

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EndWriterListener implements ItemWriteListener<Mission> {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public void afterWrite(Chunk<? extends Mission> items){
        items.forEach(this::sendEndNotification);
    }

    private void sendEndNotification(Mission mission) {
        User findUser = userRepository.findById(mission.getUser().getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
        log.info("참여자 정보 : {}", findUser.getEmail() + findUser.getName());

        if (mission.isEnded()) {
            String notificationContent = mission.getTitle() + " 미션이 종료되었습니다.";

            mission.getParticipants().forEach(participant -> {
                notificationService.sendNotification(
                        NotifyDto.builder()
                                .id(participant.getUser().getId())
                                .content(notificationContent)
                                .type(NotificationType.END)
                                .build());
            });
        }
    }
}
