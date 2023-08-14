package antmanclub.cut4userver.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentsDto {
    private Long commentId;
    private String userEmail;
    private String userName;
    private String profileImg;
    private String comment;
    private List<CommentsDto> replyCommentList;
    private int replyCommentsCount;
}
