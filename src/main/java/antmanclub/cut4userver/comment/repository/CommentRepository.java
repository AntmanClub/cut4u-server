package antmanclub.cut4userver.comment.repository;

import antmanclub.cut4userver.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
