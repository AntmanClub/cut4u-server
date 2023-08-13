package antmanclub.cut4userver.comment.domain;

import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<Comment> replyComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "POSTS_ID")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    public Comment(Comment parentComment, Posts posts, User user, String content){
        this.parentComment = parentComment;
        this.post = posts;
        this.user = user;
        this.content = content;
    }

    //답글 추가될 때 리스트에 add
    public void addReplyComment(Comment replyComment) {
        this.replyComments.add(replyComment);
    }
}
