package antmanclub.cut4userver.posts.domain;

import antmanclub.cut4userver.comment.domain.Comment;
import antmanclub.cut4userver.likes.domain.Likes;
import antmanclub.cut4userver.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Data
public class Posts extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(nullable = false)
    private String frameImg;

    private int likecount = 0;

    private int commentsCount = 0;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "image_url", nullable = false)
    @ElementCollection
    private List<String> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostsHashtag> postsHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    @Builder
    public Posts(String title, String content, String frameImg) {
        this.title = title;
        this.content = content;
        this.frameImg = frameImg;
    }

    public void addLikeCount(){
        this.likecount += 1;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void setImages(List<String> images){
        this.images = images;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }
    public void addLike(Likes likes){
        this.likes.add(likes);
    }
    public void subLikeCount(){
        this.likecount --;
    }

    public void addCommentsCount() {
        this.commentsCount++;
    }
    public void subCommentsCount(){
        this.commentsCount--;
    }
}
