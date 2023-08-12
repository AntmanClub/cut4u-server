package antmanclub.cut4userver.user.controller;

import antmanclub.cut4userver.aws.AwsUpload;
import antmanclub.cut4userver.user.dto.*;
import antmanclub.cut4userver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AwsUpload awsUpload;

    @PostMapping("/login")
    public SuccessResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }

    @PostMapping("/join")
    public SuccessResponseDto join(@RequestBody JoinRequestDto requestDto){
        return userService.join(requestDto);
    }
    @GetMapping("/duplecheck/{email}")
    public SuccessResponseDto emailDupleCheck(@PathVariable String email){
        return userService.emailDupleCheck(email);
    }
    @PatchMapping(path="/editProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserProfileUpdateResponseDto editProfile(@RequestParam(value="image") MultipartFile image,
                                                    @RequestParam(value="name") String name,
                                                    @RequestParam(value = "email") String email)
            throws IOException {
        String imgSrc = awsUpload.upload(image, "image");
        return userService.editProfile(UserProfileUpdateRequestDto.builder()
                .name(name)
                .email(email)
                .profileimg(imgSrc)
                .build());
    }
    @PostMapping("/follow")
    public SuccessResponseDto userFollow(@RequestBody UserFollowRequestDto userFollowRequestDto){
        return userService.userFollow(userFollowRequestDto);
    }
    @DeleteMapping("/unfollow")
    public SuccessResponseDto userUnfollow(@RequestBody UserFollowRequestDto userFollowRequestDto){
        return userService.userUnfollow(userFollowRequestDto);
    }
    @GetMapping("/following/list/{userId}")
    public List<FollowingListResponseDto> followingList(@PathVariable Long userId){
        return userService.followingList(userId);
    }
    @GetMapping("/follower/list/{userId}")
    public List<FollowerListResponseDto> followerList(@PathVariable Long userId){
        return userService.followerList(userId);
    }
    @GetMapping("/search/{name}")
    public List<UserListResponseDto> searchName(@PathVariable String name){
        return userService.searchName(name);
    }
}
