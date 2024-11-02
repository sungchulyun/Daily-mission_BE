package dailymissionproject.demo.domain.post.repository;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.post.dto.PostSubmitDto;
import dailymissionproject.demo.domain.post.dto.response.PostDetailResponseDto;
import dailymissionproject.demo.domain.post.dto.response.PostUserListResponseDto;
import dailymissionproject.demo.domain.user.repository.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepositoryCustom {

    List<Post> findAll();

    Slice<PostUserListResponseDto> findAllByUser(Pageable pageable, User user);

    Slice<PostDetailResponseDto> findAllByMission(Pageable pageable, Mission mission);

    //List<Post> findAllByUser(User user);

    //List<Post> findAllByMission(Mission mission);
    List<PostSubmitDto> findWeeklyPostSubmitByMission(Long id, LocalDate startDate);

    Long countPostSubmit(Mission mission, User user, LocalDateTime startDate, LocalDateTime endDate);
}
