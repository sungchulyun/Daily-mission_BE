package dailymissionproject.demo.domain.participant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "참여자 목록 DTO")
@NoArgsConstructor(force = true)
public class ParticipantUserDto {

    @Schema(description = "참여자 PK ID")
    private final Long id;
    @Schema(description = "참여자 이름")
    private final String username;
    @Schema(description = "참여자 이미지 썸네일")
    private final String imageUrl;
    @Schema(description = "참여자 강퇴여부")
    private final Boolean banned;

    @Builder
    public ParticipantUserDto(Long id, String username, String imageUrl, Boolean banned) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.banned = banned;
    }
}
