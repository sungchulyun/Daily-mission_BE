package dailymissionproject.demo.domain.mission.dto.page;

public record PageResponseDto<T> (
        T content,
        boolean next
) {
    public static <T> PageResponseDto of(T content, boolean next){
        return new PageResponseDto<>(content, next);
    }
}
