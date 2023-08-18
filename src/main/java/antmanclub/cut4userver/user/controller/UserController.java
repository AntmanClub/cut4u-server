package antmanclub.cut4userver.user.controller;

import antmanclub.cut4userver.aws.AwsUpload;
import antmanclub.cut4userver.global.result.ResultCode;
import antmanclub.cut4userver.global.result.ResultResponse;
import antmanclub.cut4userver.posts.dto.PostsAddRequestDto;
import antmanclub.cut4userver.posts.dto.PostsListResponseDto;
import antmanclub.cut4userver.user.dto.*;
import antmanclub.cut4userver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResultResponse> login(@RequestBody LoginRequestDto loginRequestDto){
        String name = userService.login(loginRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_LOGIN_SUCCESS, name));
    }

    @PostMapping("/join")
    public ResponseEntity<ResultResponse> join(@RequestBody JoinRequestDto requestDto){
        String name = userService.join(requestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_JOIN_SUCCESS, name));
    }
    @GetMapping("/duplecheck/{email}")
    public ResponseEntity<ResultResponse> emailDupleCheck(@PathVariable String email){
        String data = userService.emailDupleCheck(email);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_EMAIL_VALIDATE, data));
    }
    @PatchMapping(path="/editProfile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultResponse> editProfile(@RequestPart(value="image", required = false) MultipartFile image,
                                                    @RequestPart(value="editRequest", required = false) UserProfileUpdateRequestDto userProfileUpdateRequestDto) throws IOException {
        UserProfileUpdateResponseDto data = userService.editProfile(UserProfileUpdateRequestDto.builder()
                .name(userProfileUpdateRequestDto.getName())
                .email(userProfileUpdateRequestDto.getEmail())
                .build(), image);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_PROFILE_EDIT_SUCCESS, data));
    }
    @PostMapping("/follow")
    public ResponseEntity<ResultResponse> userFollow(@RequestBody UserFollowRequestDto userFollowRequestDto){
        String data = userService.userFollow(userFollowRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_FOLLOW_SUCCESS, data));
    }
    @DeleteMapping("/unfollow")
    public ResponseEntity<ResultResponse> userUnfollow(@RequestBody UserFollowRequestDto userFollowRequestDto){
        String data = userService.userUnfollow(userFollowRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_FOLLOW_DELETE_SUCCESS, data));
    }
    @GetMapping("/following/list/{userName}")
    public ResponseEntity<ResultResponse> followingList(@PathVariable String userName){
        List<FollowingListResponseDto> data = userService.followingList(userName);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_FOLLOWING_LIST_GET_SUCCESS, data));
    }
    @GetMapping("/follower/list/{userName}")
    public ResponseEntity<ResultResponse> followerList(@PathVariable String userName){
        List<FollowerListResponseDto> data = userService.followerList(userName);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_FOLLOWER_LIST_GET_SUCCESS, data));
    }
    @GetMapping("/search/{name}")
    public ResponseEntity<ResultResponse> searchName(@PathVariable String name){
        List<UserListResponseDto> data = userService.searchName(name);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_SEARCH_LIST_GET_SUCCESS, data));
    }
    @GetMapping("/search/hard/{name}")
    public ResponseEntity<ResultResponse> searchHardName(@PathVariable String name){
        UserListResponseDto data = userService.searchHardName(name);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_SEARCH_GET_SUCCESS, data));
    }
}
