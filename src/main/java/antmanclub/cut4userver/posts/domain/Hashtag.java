package antmanclub.cut4userver.posts.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String hashtag;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.REMOVE)
    private List<PostsHashtag> postsHashtags = new ArrayList<>();

    @Builder
    public Hashtag(String hashtag){
        this.hashtag = hashtag;
    }
}
