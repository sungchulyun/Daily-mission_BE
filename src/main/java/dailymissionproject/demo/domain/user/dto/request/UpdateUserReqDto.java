package dailymissionproject.demo.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
public class UpdateUserReqDto implements Serializable {
    private final String name;
    private final String imgUrl;

    @Builder
    UpdateUserReqDto(String name, String imgUrl){
        this.name = name;
        this.imgUrl = imgUrl;
    }
}
