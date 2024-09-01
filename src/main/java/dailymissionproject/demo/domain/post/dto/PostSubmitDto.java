package dailymissionproject.demo.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSubmitDto {

    private Long userId;
    private String nickname;
    private String imageUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    public PostSubmitDto(LocalDateTime dateTime, Long userId, String nickname, String imageUrl){
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;

        LocalDateTime check = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 3, 0,0);
        if(dateTime.isBefore(check)){
            date = dateTime.minusDays(1).toLocalDate();
        } else {
            date = dateTime.toLocalDate();
        }
    }

}
