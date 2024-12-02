package dailymissionproject.demo.domain.notify.dto.response;

import dailymissionproject.demo.domain.notify.repository.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Schema(description = "유저별 알림 응답 DTO")
public class UserNotifyResponseDto {

    @Schema(description = "알림 타입")
    private final NotificationType notificationType;
    @Schema(description = "알림 내용")
    private final String content;

    @Builder
    public UserNotifyResponseDto(NotificationType notificationType, String content) {
        this.notificationType = notificationType;
        this.content = content;
    }
}
