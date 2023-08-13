package antmanclub.cut4userver.likes.controller;

import antmanclub.cut4userver.likes.service.LikesService;
import antmanclub.cut4userver.user.dto.SuccessResponseDto;
import antmanclub.cut4userver.user.dto.UserListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {
    private final LikesService likesService;
    @PostMapping("/add/{postId}")
    public SuccessResponseDto addLike(@PathVariable Long postId){
        return likesService.addLike(postId);
    }
    @DeleteMapping("/delete/{postId}")
    public SuccessResponseDto deleteLike(@PathVariable Long postId){
        return likesService.deleteLike(postId);
    }
    @GetMapping("/userList/{postId}")
    public List<UserListResponseDto> userList(@PathVariable Long postId){
        return likesService.userList(postId);
    }
}
