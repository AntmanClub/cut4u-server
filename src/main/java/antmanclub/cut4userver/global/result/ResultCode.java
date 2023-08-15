package antmanclub.cut4userver.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // User
    USER_LOGIN_SUCCESS(200, "U001", "로그인에 성공했습니다"),
    USER_JOIN_SUCCESS(200, "U002", "회원가입에 성공했습니다."),
    USER_EMAIL_VALIDATE(200, "U003", "중복되지 않는 유효한 이메일입니다."),
    USER_PROFILE_EDIT_SUCCESS(200, "U004", "유저 프로필 수정을 완료했습니다."),
    USER_FOLLOW_SUCCESS(200, "U005", "유저 팔로우에 성공했습니다."),
    USER_FOLLOW_DELETE_SUCCESS(200, "U006", "유저 팔로우 취소를 성공했습니다."),
    USER_FOLLOWING_LIST_GET_SUCCESS(200, "U007", "유저의 팔로잉 목록을 불러왔습니다."),
    USER_FOLLOWER_LIST_GET_SUCCESS(200, "U008", "유저의 팔로워 목록을 불러왔습니다.."),
    USER_SEARCH_LIST_GET_SUCCESS(200, "U008", "이름에 해당 문자열이 포함되는 유저 리스트를 불러왔습니다."),
    USER_SEARCH_GET_SUCCESS(200, "U008", "이름에 해당하는 유저의 정보를 불러왔습니다."),

    // Posts
    POSTS_ADD_SUCCESS(200, "P001", "포스트 게시를 성공했습니다."),
    POSTS_DELETE_SUCCESS(200, "P002", "포스트 삭제에 성공했습니다."),
    POSTS_PROFILE_GET_SUCCESS(200, "P003", "유저(나 아님)의 프로필 정보를 불러왔습니다."),
    POSTS_FEED_GET_LIST_SUCCESS(200, "P004", "피드의 게시물 정보들을 불러왔습니다."),
    POSTS_GET_MY_PROFILE_SUCCESS(200, "P005", "내 프로필 정보를 불러왔습니다."),
    POSTS_SEARCH_CLICK_SUCCESS(200, "P006", "추천 게시물 10개를 좋아요 순으로 불러왔습니다."),
    POSTS_FIND_GET_SUCCESS(200, "P007", "특정 게시물을 찾는 데에 성공했습니다."),

    // Comment
    COMMENT_GET_SUCCESS(200, "C001", "특정 게시물의 댓글 정보를 불러왔습니다."),
    COMMENT_SAVE_SUCCESS(200, "C002", "댓글(답글) 등록에 성공했습니다."),
    COMMENT_DELETE_SUCCESS(200, "C003", "댓글(답글) 삭제에 성공했습니다."),
    COMMENT_MODIFY_SUCCESS(200, "C004", "댓글(답글) 수정에 성공했습니다."),

    // Like
    LIKE_SAVE_SUCCESS(200, "L001", "좋아요 등록에 성공했습니다."),
    LIKE_DELETE_SUCCESS(200, "L002", "좋아요 삭제에 성공했습니다."),
    LIKE_USER_LIST_GET_SUCCESS(200, "L003", "좋아요 누른 유저 목록을 불러왔습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
