package antmanclub.cut4userver.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentsListResponseDto {
    private List<CommentsDto> commentDtoList;

    public CommentsListResponseDto(List<CommentsDto> commentDtoList){
        this.commentDtoList = commentDtoList;
    }
}
