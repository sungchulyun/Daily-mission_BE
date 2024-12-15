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

    @Schema(description = "PK")
    private final Long id;
    @Schema(description = "알림 타입")
    private final NotificationType notificationType;
    @Schema(description = "알림 내용")
    private final String content;
    @Schema(description = "읽음 유무")
    private final boolean checked;

    @Builder
    public UserNotifyResponseDto(Long id, NotificationType notificationType, String content, boolean checked) {
        this.notificationType = notificationType;
        this.content = content;
        this.id = id;
        this.checked = checked;
    }
}
