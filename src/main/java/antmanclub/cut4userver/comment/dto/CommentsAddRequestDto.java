package antmanclub.cut4userver.comment.dto;

import lombok.Getter;

@Getter
public class CommentsAddRequestDto {
    private Long parentCommentId;
    private String comment;
}
