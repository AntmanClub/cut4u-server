package antmanclub.cut4userver.posts.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostsDto {
    private String userName;
    private String profileImg;
    private Long postsId;
    private String title;
    private String content;
    private String frameImg;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private List<String> Hashtags;
    private List<String> postImages;
}
