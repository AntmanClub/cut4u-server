package antmanclub.cut4userver.comment.dto;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class CommentsListResponseDto {
    private List<CommentsDto> commentDtoList;
    private int commentCount;
    public CommentsListResponseDto(List<CommentsDto> commentDtoList){
        this.commentDtoList = commentDtoList;
        AtomicInteger count = new AtomicInteger();
        commentDtoList.stream().forEach(commentsDto -> {
            count.getAndIncrement();
            if(commentsDto.getReplyCommentList() != null){
                commentsDto.getReplyCommentList().stream().forEach(replyComment -> {
                    count.getAndIncrement();
                });
            }
        });
        this.commentCount = count.intValue();
    }
}
