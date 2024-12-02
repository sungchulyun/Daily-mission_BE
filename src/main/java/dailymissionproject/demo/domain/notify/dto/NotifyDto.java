package dailymissionproject.demo.domain.notify.dto;

import dailymissionproject.demo.domain.notify.repository.Notification;
import dailymissionproject.demo.domain.notify.repository.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "푸시 알림 전송 DTO")
public class NotifyDto {

    @Schema(description = "수신받는 유저의 PK")
    private final Long id;
    @Schema(description = "알림 내용")
    private final String content;
    @Schema(description = "알림 타입")
    private final NotificationType type;

    @Builder
    public NotifyDto(Long id, String content, NotificationType type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }
}
