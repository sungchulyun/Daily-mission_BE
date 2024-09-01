package dailymissionproject.demo.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostHistoryDto {

    private Long userId;
    private String nickname;
    private String imageUrl;
    private Boolean banned;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> date = new ArrayList<>();

    @Builder
    public PostHistoryDto(Long userId, String nickname, String imageUrl, Boolean banned){
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.banned = banned;
    }
}
