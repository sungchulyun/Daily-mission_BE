package dailymissionproject.demo.domain.participant.repository;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantUserDto {

    private final Long id;
    private final String userName;
    private final String imgUrl;
    private final Boolean banned;

    @Builder
    public ParticipantUserDto(Long id, String userName, String imgUrl, Boolean banned) {
        this.id = id;
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.banned = banned;
    }
}
