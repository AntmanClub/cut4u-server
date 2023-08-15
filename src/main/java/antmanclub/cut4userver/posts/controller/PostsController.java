package antmanclub.cut4userver.posts.controller;

import antmanclub.cut4userver.global.result.ResultCode;
import antmanclub.cut4userver.global.result.ResultResponse;
import antmanclub.cut4userver.posts.dto.*;
import antmanclub.cut4userver.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {
    private final PostsService postsService;

    @GetMapping("/feed")
    public ResponseEntity<ResultResponse> feed(){
        PostsListResponseDto data = postsService.feed();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_FEED_GET_LIST_SUCCESS, data));
    }

    @PostMapping("/add")
    public ResponseEntity<ResultResponse> add(@RequestParam(value = "files") List<MultipartFile> files, @RequestParam(value = "title") String title,
                                    @RequestParam(value = "content") String content, @RequestParam(value = "hashTags") List<String> hashTags,
                                    @RequestParam(value = "frameImg") String frameImg){
        PostsAddRequestDto postsAddRequestDto = PostsAddRequestDto.builder()
                .title(title)
                .content(content)
                .hashtags(hashTags)
                .frameImg(frameImg)
                .build();
        PostsListResponseDto data = postsService.add(files, postsAddRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_ADD_SUCCESS, data));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse> delete(@RequestParam(value="postsId") Long postsId){
        PostsListResponseDto data = postsService.delete(postsId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_DELETE_SUCCESS, data));
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<ResultResponse> userPostsList(@PathVariable(value = "userEmail") String userEmail){
        ProfileResponseDto data = postsService.userPostsList(userEmail);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_PROFILE_GET_SUCCESS, data));
    }
    @GetMapping("/myprofile")
    public ResponseEntity<ResultResponse> myProfile(){
        ProfileResponseDto data = postsService.myProfile();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_GET_MY_PROFILE_SUCCESS, data));
    }
    @GetMapping("/search")
    public ResponseEntity<ResultResponse> searchTab(){
        List<SearchPostsResponseDto> data = postsService.searchTab();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_SEARCH_CLICK_SUCCESS, data));
    }
    @GetMapping("/search/{postId}")
    public ResponseEntity<ResultResponse> postClick(@PathVariable Long postId){
        PostsDto data = postsService.postClick(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POSTS_FIND_GET_SUCCESS, data));
    }
}
