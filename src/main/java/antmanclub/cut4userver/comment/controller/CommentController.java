package antmanclub.cut4userver.comment.controller;

import antmanclub.cut4userver.comment.domain.Comment;
import antmanclub.cut4userver.comment.dto.CommentsAddRequestDto;
import antmanclub.cut4userver.comment.dto.CommentsListResponseDto;
import antmanclub.cut4userver.comment.dto.CommentsModifyRequestDto;
import antmanclub.cut4userver.comment.service.CommentService;
import antmanclub.cut4userver.global.result.ResultCode;
import antmanclub.cut4userver.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postsId}")
    public ResponseEntity<ResultResponse> viewComments(@PathVariable(value = "postsId") Long postsId) {
        CommentsListResponseDto data = commentService.viewComments(postsId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.COMMENT_GET_SUCCESS, data));
    }

    @PostMapping("/add")
    public ResponseEntity<ResultResponse> addComments(@RequestBody CommentsAddRequestDto commentsAddRequestDto) {
        CommentsListResponseDto data = commentService.addComment(commentsAddRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.COMMENT_SAVE_SUCCESS, data));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResultResponse> deleteComments(@PathVariable(value = "commentId") Long commentId) {
        CommentsListResponseDto data = commentService.deleteComment(commentId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.COMMENT_DELETE_SUCCESS, data));
    }

    @PatchMapping("/modify")
    public ResponseEntity<ResultResponse> modifyComments(@RequestBody CommentsModifyRequestDto commentsModifyRequestDto) {
        CommentsListResponseDto data = commentService.modifyComment(commentsModifyRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.COMMENT_MODIFY_SUCCESS, data));
    }
}