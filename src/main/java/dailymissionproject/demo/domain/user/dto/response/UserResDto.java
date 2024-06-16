package dailymissionproject.demo.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResDto {
    private final String name;
    private final String email;
    private final int code;
    private final String msgCode;

    @Builder
    UserResDto(String name, String email, int code, String msgCode){
        this.name = name;
        this.email = email;
        this.code = code;
        this.msgCode = msgCode;
    }
}
