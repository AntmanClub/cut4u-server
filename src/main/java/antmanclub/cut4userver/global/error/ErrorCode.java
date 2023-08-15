package antmanclub.cut4userver.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode
{
    // User
    MEMBER_NOT_FOUND(400, "M001", "존재 하지 않는 유저입니다."),
    USERNAME_ALREADY_EXIST(400, "M002", "이미 존재하는 사용자 이름입니다."),
    ACCOUNT_MISMATCH(401, "M005", "계정 정보가 일치하지 않습니다."),

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
