package antmanclub.cut4userver.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode
{
    // User
    USER_NOT_FOUND(200, "400", "유저가 존재하지 않습니다."),
    NOT_CORRECT_PASSWORD(200, "400", "비밀번호가 틀렸습니다."),
    ALREADY_EXIST_NAME(200, "400", "이미 있는 이름입니다."),
    ALREADY_EXIST_EMAIL(200, "400", "이미 있는 이메일입니다."),
    NOT_EQUAL_PASSWORD(200, "400", "비밀번호가 일치하지 않습니다."),
    CAN_NOT_FOLLOW_MYSELF(200, "400", "자기 자신은 팔로우할 수 없습니다."),
    ALREADY_FOLLOW_USER(200, "400", "이미 팔로우한 유저입니다."),
    CAN_NOT_UNFOLLOW_MYSELF(200, "400", "자기 자신은 언팔로우할 수 없습니다."),
    NOT_FOLLOW_USER(200, "400", "팔로우하지 않은 유저입니다."),

    // Posts
    POSTS_PIC_NOT_FOUR(200, "400", "사진의 개수가 4장이 아닙니다."),
    POSTS_NOT_FOUND(200, "400", "게시물을 찾을 수 없습니다."),
    POSTS_CAN_NOT_DELETE(200, "400", "게시물을 삭제할 수 없습니다."),

    // Comment
    CAN_NOT_REPLY_COMMENT(200, "400", "답글을 등록할 수 없습니다."),
    COMMENT_NOT_FOUND(200, "400", "댓글(답글)이 존재하지 않습니다."),
    CAN_NOT_DELETE_COMMENT(200, "400", "댓글(답글)을 삭제할 수 없습니다."),
    CAN_NOT_MODIFY_COMMENT(200, "400", "댓글(답글)을 수정할 수 없습니다."),

    // Likes
    CAN_NOT_LIKE_POSTS(200, "400", "좋아요를 누를 수 없습니다."),
    CAN_NOT_UNLIKE_POSTS(200, "400", "좋아요를 취소할 수 없습니다.")
    ;

    private final int status;
    private final String code;
    private final String message;
}
