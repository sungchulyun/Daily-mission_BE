package dailymissionproject.demo.domain.notify.dto;

import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class NotifyDto {

    private final Long id;
    private final String content;
    private final NotificationType type;

    @Builder
    public NotifyDto(Long id, String content, NotificationType type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }
}
