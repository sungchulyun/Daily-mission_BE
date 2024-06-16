package dailymissionproject.demo.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResDto {
    private final String name;
    private final int code;
    private final String msgCode;

    @Builder
    UserResDto(String name, int code, String msgCode){
        this.name = name;
        this.code = code;
        this.msgCode = msgCode;
    }
}
