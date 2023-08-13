package antmanclub.cut4userver.likes.repository;

import antmanclub.cut4userver.likes.domain.Likes;
import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostAndUser(Posts posts, User user);
}
