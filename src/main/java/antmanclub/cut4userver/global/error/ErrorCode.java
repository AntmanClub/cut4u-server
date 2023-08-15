package antmanclub.cut4userver.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode
{
    // User
    USER_NOT_FOUND(400, "U001", "유저가 존재하지 않습니다."),
    NOT_CORRECT_PASSWORD(400, "U002", "비밀번호가 틀렸습니다."),
    ALREADY_EXIST_NAME(400, "M005", "이미 있는 이름입니다."),
    ALREADY_EXIST_EMAIL(400, "M005", "이미 있는 이메일입니다."),
    NOT_EQUAL_PASSWORD(400, "M005", "비밀번호가 일치하지 않습니다."),
    CAN_NOT_FOLLOW_MYSELF(400, "M005", "자기 자신은 팔로우할 수 없습니다."),
    ALREADY_FOLLOW_USER(400, "M005", "이미 팔로우한 유저입니다."),
    CAN_NOT_UNFOLLOW_MYSELF(400, "M005", "자기 자신은 언팔로우할 수 없습니다."),
    NOT_FOLLOW_USER(400, "M005", "팔로우하지 않은 유저입니다."),

    // Posts
    DAILYPLAN_ALREADY_EXIST(400, "D001", "이미 데일리 플랜을 작성하였습니다. PUT으로 update 해주세요"),
    DAILYPLAN_NOT_FOUND(400, "D002", "존재 하지 않는 데일리 플랜입니다"),
    DAILYPLAN_MONTHLIST_NOT_FOUND(400, "D003", "해당 월에 작성된 데일리 플랜이 없습니다"),
    DAILYPLAN_NOT_HAVE_TODOLIST(400, "D004", "해당 데일리 플랜에 작성된 투두리스트가 없습니다"),

    // Comment
    TODOLIST_NOT_FOUND(400, "T001", "존재 하지 않는 투두리스트입니다"),

    // Likes
    Category_NOT_FOUND(400, "C001", "존재하지 않는 카테고리입니다.")
    ;

    private final int status;
    private final String code;
    private final String message;
}
