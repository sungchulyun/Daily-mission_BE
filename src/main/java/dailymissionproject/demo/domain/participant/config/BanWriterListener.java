package dailymissionproject.demo.domain.participant.config;

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
        log.info("분기 타는지 ? ");
        for(List<Participant> participants : items){
            participants.forEach(this::sendBanNotification);
        }
    }

    private void sendBanNotification(Participant participant) {
        User findUser = userRepository.findById(participant.getUser().getId()).orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
        log.info("참여자 정보 : {}", findUser.getEmail() + findUser.getNickname());

        if(participant.isBanned()) {
            notificationService.createNotification(participant.getUser(), NotificationType.BAN, participant.getMission().getTitle() + "미션에서 인증글 미제출로 강제퇴장 처리되었습니다.");
        }
    }
}
