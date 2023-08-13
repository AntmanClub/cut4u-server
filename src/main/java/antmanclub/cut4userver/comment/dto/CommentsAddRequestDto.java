package antmanclub.cut4userver.comment.dto;

import lombok.Getter;

@Getter
public class CommentsAddRequestDto {
    private Long parentCommentId;
    private Long postsId;
    private String comment;
}
