package antmanclub.cut4userver.comment.controller;

import antmanclub.cut4userver.comment.dto.CommentsAddRequestDto;
import antmanclub.cut4userver.comment.dto.CommentsListResponseDto;
import antmanclub.cut4userver.comment.dto.CommentsModifyRequestDto;
import antmanclub.cut4userver.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postsId}")
    public CommentsListResponseDto viewComments(@PathVariable(value = "postsId") Long postsId){
        return commentService.viewComments(postsId);
    }
    @PostMapping("/add")
    public CommentsListResponseDto addComments(@RequestBody CommentsAddRequestDto commentsAddRequestDto){
        return commentService.addComment(commentsAddRequestDto);
    }

    @DeleteMapping("/delete/{commentId}")
    public CommentsListResponseDto deleteComments(@PathVariable(value = "commentId") Long commentId){
        return commentService.deleteComment(commentId);
    }

    @PatchMapping("/modify")
    public CommentsListResponseDto modifyComments(@RequestBody CommentsModifyRequestDto commentsModifyRequestDto){
        return commentService.modifyComment(commentsModifyRequestDto);
    }
}
