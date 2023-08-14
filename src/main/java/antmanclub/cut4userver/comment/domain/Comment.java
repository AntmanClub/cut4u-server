package antmanclub.cut4userver.comment.domain;

import antmanclub.cut4userver.posts.domain.BaseTimeEntity;
import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
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
