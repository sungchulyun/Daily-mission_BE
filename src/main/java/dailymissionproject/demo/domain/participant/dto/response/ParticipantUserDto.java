package dailymissionproject.demo.domain.participant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "참여자 목록 DTO")
public class ParticipantUserDto {

    @Schema(description = "참여자 PK ID")
    private final Long id;
    @Schema(description = "참여자 이름")
    private final String userName;
    @Schema(description = "참여자 이미지 썸네일")
    private final String imgUrl;
    @Schema(description = "참여자 강퇴여부")
    private final Boolean banned;

    @Builder
    public ParticipantUserDto(Long id, String userName, String imgUrl, Boolean banned) {
        this.id = id;
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.banned = banned;
    }
}
