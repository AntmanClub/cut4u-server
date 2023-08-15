package antmanclub.cut4userver.likes.controller;

import antmanclub.cut4userver.global.result.ResultCode;
import antmanclub.cut4userver.global.result.ResultResponse;
import antmanclub.cut4userver.likes.service.LikesService;
import antmanclub.cut4userver.user.dto.SuccessResponseDto;
import antmanclub.cut4userver.user.dto.UserListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {
    private final LikesService likesService;
    @PostMapping("/add/{postId}")
    public ResponseEntity<ResultResponse> addLike(@PathVariable Long postId){
        boolean data = likesService.addLike(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_SAVE_SUCCESS, data));
    }
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ResultResponse> deleteLike(@PathVariable Long postId){
        boolean data = likesService.deleteLike(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_DELETE_SUCCESS, data));
    }
    @GetMapping("/userList/{postId}")
    public ResponseEntity<ResultResponse> userList(@PathVariable Long postId){
        List<UserListResponseDto> data = likesService.userList(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_USER_LIST_GET_SUCCESS, data));
    }
}
