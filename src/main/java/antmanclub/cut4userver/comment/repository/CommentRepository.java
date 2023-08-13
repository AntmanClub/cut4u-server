package antmanclub.cut4userver.comment.repository;

import antmanclub.cut4userver.comment.domain.Comment;
import antmanclub.cut4userver.posts.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByPost(Posts posts);
}
