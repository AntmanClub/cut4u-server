package antmanclub.cut4userver.posts.controller;

import antmanclub.cut4userver.posts.dto.*;
import antmanclub.cut4userver.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {
    private final PostsService postsService;

    @GetMapping("/feed")
    public PostsListResponseDto feed(){
        return postsService.feed();
    }

    @PostMapping("/add")
    public PostsListResponseDto add(@RequestParam(value = "files") List<MultipartFile> files, @RequestParam(value = "title") String title,
                                    @RequestParam(value = "content") String content, @RequestParam(value = "hashTags") List<String> hashTags,
                                    @RequestParam(value = "frameImg") String frameImg){
        PostsAddRequestDto postsAddRequestDto = PostsAddRequestDto.builder()
                .title(title)
                .content(content)
                .hashtags(hashTags)
                .frameImg(frameImg)
                .build();
        return postsService.add(files, postsAddRequestDto);
    }

    @DeleteMapping("/delete")
    public PostsListResponseDto delete(@RequestParam(value="postsId") Long postsId){
        return postsService.delete(postsId);
    }

    @GetMapping("/{userEmail}")
    public ProfileResponseDto userPostsList(@PathVariable(value = "userEmail") String userEmail){
        return postsService.userPostsList(userEmail);
    }
    @GetMapping("/myprofile")
    public ProfileResponseDto myProfile(){
        return postsService.myProfile();
    }
    @GetMapping("/search")
    public List<SearchPostsResponseDto> searchTab(){
        return postsService.searchTab();
    }
    @GetMapping("/search/{postId}")
    public PostsDto postClick(@PathVariable Long postId){
        return postsService.postClick(postId);
    }
}
