package antmanclub.cut4userver.comment.dto;

import lombok.Getter;

@Getter
public class CommentsModifyRequestDto {
    private Long commentId;
    private String content;
}
