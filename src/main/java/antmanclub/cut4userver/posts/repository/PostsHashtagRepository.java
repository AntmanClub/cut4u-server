package antmanclub.cut4userver.posts.repository;

import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.posts.domain.PostsHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsHashtagRepository extends JpaRepository<PostsHashtag, Long> {
}
