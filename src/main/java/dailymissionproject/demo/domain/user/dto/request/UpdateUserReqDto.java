package dailymissionproject.demo.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UpdateUserReqDto implements Serializable {
    private final String name;
    private final String imageUrl;

    @Builder
    UpdateUserReqDto(String name, String imageUrl){
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
