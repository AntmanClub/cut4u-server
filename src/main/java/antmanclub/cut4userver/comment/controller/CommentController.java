package antmanclub.cut4userver.comment.controller;

import antmanclub.cut4userver.comment.domain.Comment;
import antmanclub.cut4userver.comment.dto.CommentsAddRequestDto;
import antmanclub.cut4userver.comment.dto.CommentsListResponseDto;
import antmanclub.cut4userver.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/add")
    public CommentsListResponseDto addComments(@RequestBody CommentsAddRequestDto commentsAddRequestDto){
        return commentService.addComment(commentsAddRequestDto);
    }
    @GetMapping("/{postsId}")
    public CommentsListResponseDto viewComments(@PathVariable(value = "postsId") Long postsId){
        return commentService.viewComments(postsId);
    }
}
