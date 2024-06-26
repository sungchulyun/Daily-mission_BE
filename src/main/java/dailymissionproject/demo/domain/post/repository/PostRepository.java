package dailymissionproject.demo.domain.post.repository;

import dailymissionproject.demo.domain.user.repository.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

      Post save(Post post);

      List<Post> findAllByUser(User user);
}
